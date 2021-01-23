package ch.hftm.softwarearchitektur.facefinder.service.impl;

import ch.hftm.softwarearchitektur.facefinder.common.ImageUtil;
import ch.hftm.softwarearchitektur.facefinder.domain.Image;
import ch.hftm.softwarearchitektur.facefinder.publisher.ImageSource;
import ch.hftm.softwarearchitektur.facefinder.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {

    private ImageSource source;

    @Autowired
    public ImageServiceImpl(ImageSource source) {
        this.source = source;
    }

    @Override
    public void publishImage(byte[] bytes, String contentType) throws IOException {
        if (isNotJpg(contentType))
            bytes = ImageUtil.convertToJpg(bytes);
        final Image image = Image.from(bytes);
        log.info("Publish image-upload-exchange");
        source.imageUploadChannel().send(MessageBuilder.withPayload(image).build());
    }

    private boolean isNotJpg(String contentType) {
        return !("image/jpg".equals(contentType) || "image/jpeg".equals(contentType));
    }
}
