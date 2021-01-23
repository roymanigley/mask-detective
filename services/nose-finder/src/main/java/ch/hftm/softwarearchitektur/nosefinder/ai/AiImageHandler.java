package ch.hftm.softwarearchitektur.nosefinder.ai;

import ch.hftm.softwarearchitektur.nosefinder.ai.classifier.NoseFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface AiImageHandler {

    Set<Nose> imageNoseDetection(NoseFinderClassifierAdapter classifier, byte[] imageBytes, Path path) throws IOException;

    byte[] drawRectanglesAroundNoses(FacesResult facesResult) throws IOException;
}
