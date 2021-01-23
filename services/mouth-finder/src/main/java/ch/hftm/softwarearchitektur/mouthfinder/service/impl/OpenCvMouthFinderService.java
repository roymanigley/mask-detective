package ch.hftm.softwarearchitektur.mouthfinder.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import ch.hftm.softwarearchitektur.mouthfinder.common.FileUtil;
import ch.hftm.softwarearchitektur.mouthfinder.common.ImageUtil;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Face;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;
import ch.hftm.softwarearchitektur.mouthfinder.publisher.MouthFinderSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import ch.hftm.softwarearchitektur.mouthfinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.mouthfinder.ai.classifier.MouthFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.mouthfinder.service.MouthFinderService;

@Service
@Log4j2
public class OpenCvMouthFinderService implements MouthFinderService {

    private final MouthFinderClassifierAdapter classifier;
    private final AiImageHandler aiImageHandler;
    private final MouthFinderSource mouthFinderSource;

    public OpenCvMouthFinderService(MouthFinderClassifierAdapter classifier, AiImageHandler aiImageHandler, MouthFinderSource mouthFinderSource) {
        this.classifier = classifier;
        this.aiImageHandler = aiImageHandler;
        this.mouthFinderSource = mouthFinderSource;
    }

    @Override
    public void processFacesResult(FacesResult facesResult) throws IOException {
        final BufferedImage originalImage = ImageUtil.getOriginalImageFromFacesResult(facesResult);
        appendMouthToFace(facesResult, originalImage);

        final boolean hasFacesWithOutMouth = facesResult.getFacesDetected().stream()
                .anyMatch(face -> !face.isMouthDetected());

        byte[] imageWithRectangles = aiImageHandler.drawRectanglesAroundMouths(facesResult);
        final String encoded = Base64.getEncoder().encodeToString(imageWithRectangles);
        facesResult.setImageWithRectangles(encoded);

        if (!hasFacesWithOutMouth) {
            log.info("Publish mouth-found-exchange");
            mouthFinderSource.mouthFoundChannel().send(MessageBuilder.withPayload(facesResult).build());
        } else {
            log.info("Publish no-mouth-found-exchange");
            mouthFinderSource.noMouthFoundChannel().send(MessageBuilder.withPayload(facesResult).build());
        }
    }

    private void appendMouthToFace(FacesResult facesResult, BufferedImage originalImage) {
        facesResult.getFacesDetected().stream().forEach(face -> {
            try {
                adaptFaceDimensions(originalImage, face);
                final BufferedImage bufferedImage = ImageUtil.corpFaceFromImage(originalImage, face);
                final byte[] bytes = ImageUtil.imageToBytes(bufferedImage);
                findMouth(bytes, "jpg", face).stream()
                        .forEach(face.getMouthsDetected()::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void adaptFaceDimensions(BufferedImage originalImage, Face face) {
        final int x = Math.max(face.getX() - face.getWidth() / 5, 0);
        final int y = Math.max(face.getY() - face.getHeight() / 5, 0);
        final int width = Math.min(face.getWidth() + face.getWidth() / 5 * 2, originalImage.getWidth() - x);
        final int height = Math.min(face.getHeight() + face.getHeight() / 5 * 2, originalImage.getHeight() - y);
        face.setX(x);
        face.setY(y);
        face.setWidth(width);
        face.setHeight(height);
    }

    @Override
    public Set<Mouth> findMouth(byte[] imageBytes, String imageType, Face face) throws IOException {
        Path tempFile = FileUtil.createTempFile(imageType);
        try {
            Set<Mouth> mouthDetected = aiImageHandler.imageMouthDetection(classifier, imageBytes, tempFile).stream()
                    .filter(mouth -> mouth.getY() > face.getHeight() * 0.6)
                    .collect(Collectors.toSet());
            return mouthDetected;
        } finally {
            FileUtil.deleteSafe(tempFile);
        }
    }
}
