package ch.hftm.softwarearchitektur.mouthfinder.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FacesResult {

    private String imageOriginal;

    @Setter
    private String imageWithRectangles;

    @Builder.Default
    private Set<Face> facesDetected = new HashSet<>();
}
