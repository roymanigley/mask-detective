package ch.hftm.softwarearchitektur.nosefinder.service.impl;

import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;
import org.opencv.core.Rect;

public class OpenCvResultMapper {

    public static Nose toNose(Rect rect) {
        return Nose.builder()
                .x(rect.x)
                .y(rect.y)
                .height(rect.height)
                .width(rect.width)
                .build();
    }
}
