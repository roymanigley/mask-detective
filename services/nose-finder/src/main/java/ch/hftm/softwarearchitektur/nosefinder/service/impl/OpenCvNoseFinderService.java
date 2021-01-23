package ch.hftm.softwarearchitektur.nosefinder.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import ch.hftm.softwarearchitektur.nosefinder.common.FileUtil;
import ch.hftm.softwarearchitektur.nosefinder.common.ImageUtil;
import ch.hftm.softwarearchitektur.nosefinder.domain.Face;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;
import ch.hftm.softwarearchitektur.nosefinder.publisher.NoseFinderSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import ch.hftm.softwarearchitektur.nosefinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.nosefinder.ai.classifier.NoseFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.nosefinder.service.NoseFinderService;

@Service
@Log4j2
public class OpenCvNoseFinderService implements NoseFinderService {

    private final NoseFinderClassifierAdapter classifier;
    private final AiImageHandler aiImageHandler;
    private final NoseFinderSource noseFinderSource;

    public OpenCvNoseFinderService(NoseFinderClassifierAdapter classifier, AiImageHandler aiImageHandler, NoseFinderSource noseFinderSource) {
        this.classifier = classifier;
        this.aiImageHandler = aiImageHandler;
        this.noseFinderSource = noseFinderSource;
    }

    @Override
    public void processFacesResult(FacesResult facesResult) throws IOException {
        final BufferedImage originalImage = ImageUtil.getOriginalImageFromFacesResult(facesResult);
        appendNoseToFace(facesResult, originalImage);

        final boolean hasMouthOrNoseDetected = facesResult.getFacesDetected().stream()
                .anyMatch(face -> face.isMouthDetected() || face.isNoseDetected());

        byte[] imageWithRectangles = aiImageHandler.drawRectanglesAroundNoses(facesResult);
        final String encoded = Base64.getEncoder().encodeToString(imageWithRectangles);
        facesResult.setImageWithRectangles(encoded);

        if (hasMouthOrNoseDetected) {
            log.info("Publish nose-found-exchange");
            noseFinderSource.noseFoundChannel().send(MessageBuilder.withPayload(facesResult).build());
        } else {
            // log.info("Publish no-nose-found-exchange");
            // noseFinderSource.noNoseFoundChannel().send(MessageBuilder.withPayload(facesResult).build());
        }
    }

    private void appendNoseToFace(FacesResult facesResult, BufferedImage originalImage) {
        facesResult.getFacesDetected().stream().forEach(face -> {
            try {
                // adaptFaceDimensions(originalImage, face);
                final BufferedImage bufferedImage = ImageUtil.corpFaceFromImage(originalImage, face);
                final byte[] bytes = ImageUtil.imageToBytes(bufferedImage);
                findNose(bytes, "jpg", face).stream()
                        .forEach(face.getNosesDetected()::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void adaptFaceDimensions(BufferedImage originalImage, Face face) {
        final int x = Math.max(face.getX() - face.getWidth() / 5, 0);
        final int y = Math.max(face.getY() - face.getHeight() / 5, 0);
        final int width = Math.min(face.getWidth() + face.getWidth() / 5 * 2, originalImage.getWidth() - 1);
        final int height = Math.min(face.getHeight() + face.getHeight() / 5 * 2, originalImage.getHeight() - 1);
        face.setX(x);
        face.setY(y);
        face.setWidth(width);
        face.setHeight(height);
    }

    @Override
    public Set<Nose> findNose(byte[] imageBytes, String imageType, Face face) throws IOException {
        Path tempFile = FileUtil.createTempFile(imageType);
        try {
            Set<Nose> noseDetected = aiImageHandler.imageNoseDetection(classifier, imageBytes, tempFile).stream()
                    .filter(nose -> nose.getY() < face.getHeight() * 0.7)
                    .filter(nose -> nose.getY() > face.getHeight() * 0.3)
                    .collect(Collectors.toSet());
            return noseDetected;
        } finally {
            FileUtil.deleteSafe(tempFile);
        }
    }
}
