package ch.hftm.softwarearchitektur.facefinder.service.impl;

import java.util.Base64;
import java.util.Set;
import java.util.Base64.Encoder;

import org.opencv.core.Rect;

import ch.hftm.softwarearchitektur.facefinder.domain.Face;
import ch.hftm.softwarearchitektur.facefinder.domain.Result;

public class OpenCvResultMapper {
    
    public static Result toResult(Set<Face> facesDetected, byte[] imageOriginal, byte[] imageWithRectangles) {
        Encoder encoder = Base64.getEncoder();
        return Result.builder()
            .facesDetected(facesDetected)
            .imageOriginal(encoder.encodeToString(imageOriginal))
            .imageWithRectangles(encoder.encodeToString(imageWithRectangles))
            .build();
    }


    public static Face toFace(Rect rect) {
        return Face.builder()
            .x(rect.x)
            .y(rect.y)
            .height(rect.height)
            .width(rect.width)
            .build();
    }
}
