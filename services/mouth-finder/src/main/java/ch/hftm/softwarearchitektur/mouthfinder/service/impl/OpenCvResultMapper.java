package ch.hftm.softwarearchitektur.mouthfinder.service.impl;

import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;
import org.opencv.core.Rect;

public class OpenCvResultMapper {

    public static Mouth toMouth(Rect rect) {
        return Mouth.builder()
                .x(rect.x)
                .y(rect.y)
                .height(rect.height)
                .width(rect.width)
                .build();
    }
}
