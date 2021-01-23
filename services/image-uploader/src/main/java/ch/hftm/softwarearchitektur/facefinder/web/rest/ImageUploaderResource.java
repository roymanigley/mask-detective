package ch.hftm.softwarearchitektur.facefinder.web.rest;

import ch.hftm.softwarearchitektur.facefinder.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image-upload")
@Log4j2
public class ImageUploaderResource {

    private final ImageService service;

    public ImageUploaderResource(ImageService service) {
        this.service = service;
    }
    
    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            service.publishImage(file.getBytes(), file.getContentType());
            log.info("handleFileUpload: result published to: imageUploadChannel");
            return ResponseEntity
                    .ok()
                    .header("Content-Type", file.getContentType())
                    .body(file.getBytes());
        } catch (IOException e) {
            log.error("handleFileUpload: /api/image-upload 500", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }
}
