package ch.hftm.softwarearchitektur.mouthfinder.common;

import ch.hftm.softwarearchitektur.mouthfinder.domain.Face;
import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    public static BufferedImage getOriginalImageFromFacesResult(FacesResult facesResult) throws IOException {
        final byte[] decoded = Base64.getDecoder().decode(facesResult.getImageOriginal());
        final BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(decoded));
        return originalImage;
    }

    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    public static BufferedImage corpFaceFromImage(BufferedImage originalImage, Face face) {
        final int x = face.getX();
        final int y = face.getY();
        final int width = face.getWidth();
        final int height = face.getHeight();
        return originalImage.getSubimage(
                x, y,
                width, height);
    }
}
