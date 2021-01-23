package ch.hftm.softwarearchitektur.facefinder.subscriber;

import ch.hftm.softwarearchitektur.facefinder.domain.Image;
import ch.hftm.softwarearchitektur.facefinder.service.FaceFinderService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.io.IOException;

@EnableBinding(Sink.class)
public class ImageUploadConsumer {

    private final FaceFinderService mouthFinderService;

    public ImageUploadConsumer(FaceFinderService mouthFinderService) {
        this.mouthFinderService = mouthFinderService;
    }

    @StreamListener(target = Sink.INPUT)
    public void processFaces(Image image) {
        try {
            mouthFinderService.processImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
