package ch.hftm.softwarearchitektur.mouthfinder.service;

import java.io.IOException;
import java.util.Set;

import ch.hftm.softwarearchitektur.mouthfinder.domain.Face;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;

public interface MouthFinderService {

    void processFacesResult(FacesResult facesResult) throws IOException;
    Set<Mouth> findMouth(byte[] imageBytes, String imageType, Face face) throws IOException;
}
