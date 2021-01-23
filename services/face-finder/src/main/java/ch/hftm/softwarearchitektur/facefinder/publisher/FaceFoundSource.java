package ch.hftm.softwarearchitektur.facefinder.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface FaceFoundSource {

    @Output("faceFoundChannel")
    MessageChannel faceFoundChannel();
}
