package ch.hftm.softwarearchitektur.facefinder.ai.classifier;

import java.nio.file.Path;
import java.util.Set;

import ch.hftm.softwarearchitektur.facefinder.domain.Face;

public interface FaceFinderClassifierAdapter {
    
    public Set<Face> detectFaces(Path tempFile);
}
