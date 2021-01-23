package ch.hftm.softwarearchitektur.nosefinder.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Face {
    
    private Integer x, y, width, height;

    private boolean isMouthDetected;

    @Builder.Default
    private Set<Nose> nosesDetected = new HashSet<>();

    public Point topLeft() {
        return new Point(x, y);
    }

    public Point bottomRight() {
        return new Point(x + width, y + height);
    }

    @JsonSerialize
    public boolean isNoseDetected() {
        return !nosesDetected.isEmpty();
    }

    @Getter
    public static class Point {
        private Integer x, y;

        public Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }
}
