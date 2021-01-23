package ch.hftm.softwarearchitektur.mouthfinder.domain;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mouth {
    
    private Integer x, y, width, height;

    public Face.Point topLeft() {
        return new Face.Point(x, y);
    }

    public Face.Point bottomRight() {
        return new Face.Point(x + width, y + height);
    }
}
