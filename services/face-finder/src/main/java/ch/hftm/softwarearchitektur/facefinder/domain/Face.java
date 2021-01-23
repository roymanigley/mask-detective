package ch.hftm.softwarearchitektur.facefinder.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Face {
    
    private Integer x, y, width, height;
    private Point topLeft, bottomRight;

    public Point topLeft() {
        return new Point(x, y);
    }

    public Point bottomRight() {
        return new Point(x + width, y + height);
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
