package ch.hftm.softwarearchitektur.facefinder.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static byte[] convertToJpg(byte[] bytes) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final BufferedImage image = ImageIO.read(inputStream);
        final BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(result, "jpg", outputStream);
        return outputStream.toByteArray();
    }
}
