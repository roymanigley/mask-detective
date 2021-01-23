package ch.hftm.softwarearchitektur.mouthfinder.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MouthFinderSource {

    @Output("mouthFoundChannel")
    MessageChannel mouthFoundChannel();

    @Output("noMouthFoundChannel")
    MessageChannel noMouthFoundChannel();
}
