package ch.hftm.softwarearchitektur.nosefinder.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NoseFinderSource {

    @Output("noseFoundChannel")
    MessageChannel noseFoundChannel();

    @Output("noNoseFoundChannel")
    MessageChannel noNoseFoundChannel();
}
