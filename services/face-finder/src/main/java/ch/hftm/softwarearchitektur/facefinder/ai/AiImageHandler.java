package ch.hftm.softwarearchitektur.facefinder.ai;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import ch.hftm.softwarearchitektur.facefinder.ai.classifier.FaceFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.facefinder.domain.Face;

public interface AiImageHandler {

    Set<Face> imageFaceDetection(FaceFinderClassifierAdapter classifier, byte[] imageBytes, Path path) throws IOException;

    byte[] drawRectanglesAroundFaces(Set<Face> facesDetected, Path imagePath) throws IOException;
}
