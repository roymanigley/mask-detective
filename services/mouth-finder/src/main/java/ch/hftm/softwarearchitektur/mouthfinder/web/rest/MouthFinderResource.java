package ch.hftm.softwarearchitektur.mouthfinder.web.rest;

import ch.hftm.softwarearchitektur.mouthfinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.mouthfinder.common.FileUtil;
import ch.hftm.softwarearchitektur.mouthfinder.common.ImageUtil;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Face;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;
import ch.hftm.softwarearchitektur.mouthfinder.service.MouthFinderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/find-mouth")
@Log4j2
public class MouthFinderResource {

    private final MouthFinderService service;
    private final AiImageHandler aiImageHandler;

    public MouthFinderResource(MouthFinderService service, AiImageHandler aiImageHandler) {
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

            service.findMouth(file.getBytes(), mappedImageType, face).stream()
                .forEach(face.getMouthsDetected()::add);

            Path tempFile = FileUtil.createTempFile(mappedImageType);
            FileUtil.write(tempFile, file.getBytes());
            byte[] imageWithRectangles = aiImageHandler.drawRectanglesAroundMouths(facesResult);
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
