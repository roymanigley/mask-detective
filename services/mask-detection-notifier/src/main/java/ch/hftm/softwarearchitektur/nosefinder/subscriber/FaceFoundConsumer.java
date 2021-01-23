package ch.hftm.softwarearchitektur.nosefinder.subscriber;

import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;
import ch.hftm.softwarearchitektur.nosefinder.service.MaskDetectionNotificationService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import javax.mail.MessagingException;

@EnableBinding(NotificationSink.class)
public class FaceFoundConsumer {

    private final MaskDetectionNotificationService notificationService;

    public FaceFoundConsumer(MaskDetectionNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @StreamListener(target = "inputNose")
    public void processFacesWithNose(FacesResult result) {
        try {
            notificationService.processFacesResult(result);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @StreamListener(target = "inputMouth")
    public void processFacesWithMouth(FacesResult result) {
        try {
            notificationService.processFacesResult(result);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
