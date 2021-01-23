package ch.hftm.softwarearchitektur.nosefinder.web.rest;

import ch.hftm.softwarearchitektur.nosefinder.domain.Face;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.service.MaskDetectionNotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/send-mail")
@Log4j2
public class EmailResource {

    private final MaskDetectionNotificationService service;

    public EmailResource(MaskDetectionNotificationService service) {
        this.service = service;
    }

    @PostMapping(path = "/test", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity handleManualFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            final String encoded = Base64.getEncoder().encodeToString(file.getBytes());
            final FacesResult facesResult = FacesResult.builder().imageOriginal(encoded).imageWithRectangles(encoded).build();
            final Face face = Face.builder().x(0).y(0).width(image.getWidth()).height(image.getHeight()).build();
            facesResult.getFacesDetected().add(face);
            service.processFacesResult(facesResult);
            return ResponseEntity.ok().build();
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}
