package ch.hftm.softwarearchitektur.facefinder.web.rest;

import ch.hftm.softwarearchitektur.facefinder.domain.Result;
import ch.hftm.softwarearchitektur.facefinder.publisher.FaceFoundSource;
import ch.hftm.softwarearchitektur.facefinder.service.FaceFinderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api/find-faces")
@Log4j2
public class FaceFinderResource {

    private final FaceFinderService service;

    private final FaceFoundSource faceFoundSource;

    public FaceFinderResource(FaceFinderService service, FaceFoundSource faceFoundSource) {
        this.service = service;
        this.faceFoundSource = faceFoundSource;
    }
    
    @PostMapping(path = "/test", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] handleManualFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String mappedImageType = ContentTypeMapper.mapContentType(file.getContentType());
            Result result = service.findFaces(file.getBytes(), mappedImageType);
            faceFoundSource.faceFoundChannel().send(MessageBuilder.withPayload(result).build());
            String base64 = result.getImageWithRectangles();
            return Base64.getDecoder().decode(base64);
        } catch (Exception e) {
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
