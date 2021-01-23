package ch.hftm.softwarearchitektur.nosefinder.ai.impl;

import ch.hftm.softwarearchitektur.nosefinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.nosefinder.ai.classifier.NoseFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.nosefinder.common.FileUtil;
import ch.hftm.softwarearchitektur.nosefinder.domain.Face;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;
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
    public Set<Nose> imageNoseDetection(NoseFinderClassifierAdapter classifier, byte[] imageBytes, Path path)
            throws IOException {
        FileUtil.write(path, imageBytes);
        final Set<Nose> noses = classifier.detectNoses(path);
        if (log.isDebugEnabled())
            log.debug("noses found: " + noses.size());
        return noses;
    }

    @Override
    public byte[] drawRectanglesAroundNoses(FacesResult facesResult) throws IOException {
            Path tempFile = null;
            Path tempFileRaw = null;
            try {
                tempFileRaw = FileUtil.createTempFile("jpg");
                final byte[] decoded = Base64.getDecoder().decode(facesResult.getImageWithRectangles());
                FileUtil.write(tempFileRaw, decoded);
                Mat loadedImage = loadedImage(tempFileRaw);
                for (Face face : facesResult.getFacesDetected()) {
                    for (Nose nose : face.getNosesDetected()) {
                        Point topLeft = new Point(face.topLeft().getX() + nose.topLeft().getX(), face.topLeft().getY() + nose.topLeft().getY());
                        Point bottomRight = new Point(face.topLeft().getX() + nose.bottomRight().getX(), face.topLeft().getY() + nose.bottomRight().getY());
                        Imgproc.rectangle(loadedImage, topLeft, bottomRight, new Scalar(255, 0, 0), 1);
                    }
                }
                tempFile = FileUtil.createTempFile("faces_with_nose_", ".jpg");
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
