package ch.hftm.softwarearchitektur.nosefinder.service;

import ch.hftm.softwarearchitektur.nosefinder.domain.Face;
import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;

import java.io.IOException;
import java.util.Set;

public interface NoseFinderService {

    void processFacesResult(FacesResult facesResult) throws IOException;
    Set<Nose> findNose(byte[] imageBytes, String imageType, Face face) throws IOException;
}
