package ch.hftm.softwarearchitektur.mouthfinder.ai;

import ch.hftm.softwarearchitektur.mouthfinder.ai.classifier.MouthFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface AiImageHandler {

    Set<Mouth> imageMouthDetection(MouthFinderClassifierAdapter classifier, byte[] imageBytes, Path path) throws IOException;

    byte[] drawRectanglesAroundMouths(FacesResult facesResult) throws IOException;
}
