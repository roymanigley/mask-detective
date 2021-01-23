package ch.hftm.softwarearchitektur.nosefinder.subscriber;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NotificationSink {

    @Input("inputNose")
    SubscribableChannel inputNose();

    @Input("inputMouth")
    SubscribableChannel inputMouth();
}
