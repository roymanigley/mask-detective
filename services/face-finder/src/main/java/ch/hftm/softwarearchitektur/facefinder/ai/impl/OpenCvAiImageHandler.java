package ch.hftm.softwarearchitektur.facefinder.ai.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import ch.hftm.softwarearchitektur.facefinder.common.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import ch.hftm.softwarearchitektur.facefinder.ai.AiImageHandler;
import ch.hftm.softwarearchitektur.facefinder.ai.classifier.FaceFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.facefinder.domain.Face;

@Component
@Log4j2
public class OpenCvAiImageHandler implements AiImageHandler {

    @Override
    public Set<Face> imageFaceDetection(FaceFinderClassifierAdapter classifier, byte[] imageBytes, Path path)
            throws IOException {
        FileUtil.write(path, imageBytes);
        final Set<Face> faces = classifier.detectFaces(path);
        if (log.isDebugEnabled())
            log.debug("faces found: " + faces.size());
        return faces;
    }

    @Override
    public byte[] drawRectanglesAroundFaces(Set<Face> facesDetected, Path imagePath) throws IOException {
        Path tempFile = null;
        try {
            Mat loadedImage = loadedImage(imagePath);
            for (Face face : facesDetected) {
                Point topLeft = new Point(face.topLeft().getX(), face.topLeft().getY());
                Point bottomRight = new Point(face.bottomRight().getX(), face.bottomRight().getY());
               Imgproc.rectangle(loadedImage, topLeft, bottomRight, new Scalar(0, 0, 255), 1);
            }
            tempFile = FileUtil.createTempFile("faces_", "jpg");
            Imgcodecs.imwrite(tempFile.toString(), loadedImage);
            return FileUtil.readAllBytes(tempFile);
        } finally {
            FileUtil.deleteSafe(imagePath, tempFile);
        }
    }

    private Mat loadedImage(Path imagePath) {
        return Imgcodecs.imread(imagePath.toString());
    }
}
