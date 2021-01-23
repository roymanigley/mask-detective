package ch.hftm.softwarearchitektur.facefinder.service;

import java.io.IOException;

import ch.hftm.softwarearchitektur.facefinder.domain.Image;
import ch.hftm.softwarearchitektur.facefinder.domain.Result;

public interface FaceFinderService {
    
    Result findFaces(byte[] imageBytes, String imageType) throws IOException;

    void processImage(Image image) throws IOException;
}
