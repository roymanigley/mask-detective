package ch.hftm.softwarearchitektur.facefinder.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ImageSource {

    @Output("imageUploadChannel")
    MessageChannel imageUploadChannel();
}
