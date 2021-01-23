package ch.hftm.softwarearchitektur.facefinder.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Set;

import ch.hftm.softwarearchitektur.facefinder.common.FileUtil;
import ch.hftm.softwarearchitektur.facefinder.domain.Image;
import ch.hftm.softwarearchitektur.facefinder.publisher.FaceFoundSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import ch.hftm.softwarearchitektur.facefinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.facefinder.ai.classifier.FaceFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.facefinder.domain.Face;
import ch.hftm.softwarearchitektur.facefinder.domain.Result;
import ch.hftm.softwarearchitektur.facefinder.service.FaceFinderService;

@Service
@Log4j2
public class OpenCvFaceFinderService implements FaceFinderService {

    private final FaceFinderClassifierAdapter classifier;
    private final AiImageHandler aiImageHandler;
    private final FaceFoundSource faceFoundSource;

    public OpenCvFaceFinderService(FaceFinderClassifierAdapter classifier, AiImageHandler aiImageHandler, FaceFoundSource faceFoundSource) {
        this.classifier = classifier;
        this.aiImageHandler = aiImageHandler;
        this.faceFoundSource = faceFoundSource;
    }

    @Override
    public Result findFaces(byte[] imageBytes, String imageType) throws IOException {
        Path tempFile = FileUtil.createTempFile(imageType);
        Set<Face> facesDetected = aiImageHandler.imageFaceDetection(classifier, imageBytes, tempFile);
        byte[] imageWithRectangles = aiImageHandler.drawRectanglesAroundFaces(facesDetected, tempFile);
        return OpenCvResultMapper.toResult(facesDetected, imageBytes, imageWithRectangles);
    }

    @Override
    public void processImage(Image image) throws IOException {
        final byte[] bytes = Base64.getDecoder().decode(image.getImageBase64());
        Result result = findFaces(bytes, "jpg");
        if (!result.getFacesDetected().isEmpty()) {
            log.info("Publish face-found-exchange");
            faceFoundSource.faceFoundChannel().send(MessageBuilder.withPayload(result).build());
        } else {
            log.info("nothing to Publish, no faces found");
        }
    }
}
