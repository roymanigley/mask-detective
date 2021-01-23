package ch.hftm.softwarearchitektur.facefinder.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Base64;

@Getter
@ToString
public class Image {
    
    private String imageBase64;

    private Image(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public static Image from(byte[] bytes) {
        final String encoded = Base64.getEncoder().encodeToString(bytes);
        return new Image(encoded);
    }
}
