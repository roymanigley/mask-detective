package ch.hftm.softwarearchitektur.mouthfinder.ai.classifier;

import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;

import java.nio.file.Path;
import java.util.Set;

public interface MouthFinderClassifierAdapter {
    
    Set<Mouth> detectMouths(Path tempFile);
}
