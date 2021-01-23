package ch.hftm.softwarearchitektur.facefinder.service;

import java.io.IOException;

public interface ImageService {

    void publishImage(byte[] bytes, String contentType) throws IOException;
}
