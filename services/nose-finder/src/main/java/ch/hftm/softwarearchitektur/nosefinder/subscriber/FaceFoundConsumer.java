package ch.hftm.softwarearchitektur.nosefinder.subscriber;

import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.service.NoseFinderService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.io.IOException;

@EnableBinding(Sink.class)
public class FaceFoundConsumer {

    private final NoseFinderService noseFinderService;

    public FaceFoundConsumer(NoseFinderService noseFinderService) {
        this.noseFinderService = noseFinderService;
    }

    @StreamListener(target = Sink.INPUT)
    public void processFaces(FacesResult result) {
        try {
            noseFinderService.processFacesResult(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
