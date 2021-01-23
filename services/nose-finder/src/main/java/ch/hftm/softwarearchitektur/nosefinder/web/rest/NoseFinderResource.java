package ch.hftm.softwarearchitektur.nosefinder.web.rest;

import ch.hftm.softwarearchitektur.nosefinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.nosefinder.common.FileUtil;
import ch.hftm.softwarearchitektur.nosefinder.domain.Face;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.service.NoseFinderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Base64;

@RestController
@RequestMapping("/api/find-nose")
@Log4j2
public class NoseFinderResource {

    private final NoseFinderService service;
    private final AiImageHandler aiImageHandler;

    public NoseFinderResource(NoseFinderService service, AiImageHandler aiImageHandler) {
        this.service = service;
        this.aiImageHandler = aiImageHandler;
    }

    @PostMapping(path = "/test", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] handleManualFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String mappedImageType = ContentTypeMapper.mapContentType(file.getContentType());

            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            final String encoded = Base64.getEncoder().encodeToString(file.getBytes());
            final FacesResult facesResult = FacesResult.builder().imageOriginal(encoded).imageWithRectangles(encoded).build();
            final Face face = Face.builder().x(0).y(0).width(image.getWidth()).height(image.getHeight()).build();
            facesResult.getFacesDetected().add(face);

            service.findNose(file.getBytes(), mappedImageType, face).stream()
                .forEach(face.getNosesDetected()::add);

            Path tempFile = FileUtil.createTempFile(mappedImageType);
            FileUtil.write(tempFile, file.getBytes());
            byte[] imageWithRectangles = aiImageHandler.drawRectanglesAroundNoses(facesResult);
            return imageWithRectangles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static class ContentTypeMapper {
        public static String mapContentType(String contentType) throws IllegalArgumentException {
            switch (contentType) {
                case "image/jpg": return "jpg";
                case "image/jpeg": return "jpeg";
                default: throw new IllegalArgumentException("Unsupported image type: " + contentType);
            }
        }
    }
}
