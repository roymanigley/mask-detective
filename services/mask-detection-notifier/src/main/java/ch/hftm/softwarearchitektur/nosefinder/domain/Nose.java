package ch.hftm.softwarearchitektur.nosefinder.domain;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Nose {
    
    private Integer x, y, width, height;
    private Face.Point topLeft, bottomRight;

    public Face.Point topLeft() {
        return new Face.Point(x, y);
    }

    public Face.Point bottomRight() {
        return new Face.Point(x + width, y + height);
    }
}
