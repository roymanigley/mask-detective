package ch.hftm.softwarearchitektur.nosefinder.ai.classifier;

import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;

import java.nio.file.Path;
import java.util.Set;

public interface NoseFinderClassifierAdapter {
    
    Set<Nose> detectNoses(Path tempFile);
}
