package ch.hftm.softwarearchitektur.mouthfinder.subscriber;

import ch.hftm.softwarearchitektur.mouthfinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.mouthfinder.service.MouthFinderService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.io.IOException;

@EnableBinding(Sink.class)
public class FaceFoundConsumer {

    private final MouthFinderService mouthFinderService;

    public FaceFoundConsumer(MouthFinderService mouthFinderService) {
        this.mouthFinderService = mouthFinderService;
    }

    @StreamListener(target = Sink.INPUT)
    public void processFaces(FacesResult result) {
        try {
            mouthFinderService.processFacesResult(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
