package ch.hftm.softwarearchitektur.facefinder.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Result {

    private String imageOriginal, imageWithRectangles;

    @Builder.Default
    private Set<Face> facesDetected = new HashSet<>();
}
