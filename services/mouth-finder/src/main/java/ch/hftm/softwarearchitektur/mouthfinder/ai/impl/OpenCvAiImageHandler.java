package ch.hftm.softwarearchitektur.mouthfinder.ai.impl;

import ch.hftm.softwarearchitektur.mouthfinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.mouthfinder.ai.classifier.MouthFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.mouthfinder.common.FileUtil;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Face;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Set;

@Component
@Log4j2
public class OpenCvAiImageHandler implements AiImageHandler {

    @Override
    public Set<Mouth> imageMouthDetection(MouthFinderClassifierAdapter classifier, byte[] imageBytes, Path path)
            throws IOException {
        FileUtil.write(path, imageBytes);
        final Set<Mouth> mouths = classifier.detectMouths(path);
        if (log.isDebugEnabled())
            log.debug("mouths found: " + mouths.size());
        return mouths;
    }

    @Override
    public byte[] drawRectanglesAroundMouths(FacesResult facesResult) throws IOException {
            Path tempFile = null;
            Path tempFileRaw = null;
            try {
                tempFileRaw = FileUtil.createTempFile("jpg");
                final byte[] decoded = Base64.getDecoder().decode(facesResult.getImageWithRectangles());
                FileUtil.write(tempFileRaw, decoded);
                Mat loadedImage = loadedImage(tempFileRaw);
                for (Face face : facesResult.getFacesDetected()) {
                    for (Mouth mouth : face.getMouthsDetected()) {
                        Point topLeft = new Point(face.topLeft().getX() + mouth.topLeft().getX(), face.topLeft().getY() + mouth.topLeft().getY());
                        Point bottomRight = new Point(face.topLeft().getX() + mouth.bottomRight().getX(), face.topLeft().getY() + mouth.bottomRight().getY());
                        Imgproc.rectangle(loadedImage, topLeft, bottomRight, new Scalar(0, 255, 0), 1);
                    }
                }
                tempFile = FileUtil.createTempFile("faces_with_mouth_", ".jpg");
                Imgcodecs.imwrite(tempFile.toString(), loadedImage);
                return FileUtil.readAllBytes(tempFile);
            } finally {
                FileUtil.deleteSafe(tempFile, tempFileRaw);
            }
        }

    private Mat loadedImage(Path imagePath) {
        return Imgcodecs.imread(imagePath.toString());
    }
}
