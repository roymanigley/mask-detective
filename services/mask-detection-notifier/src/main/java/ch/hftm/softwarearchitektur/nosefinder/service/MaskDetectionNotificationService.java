package ch.hftm.softwarearchitektur.nosefinder.service;

import ch.hftm.softwarearchitektur.nosefinder.domain.FacesResult;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MaskDetectionNotificationService {

    void processFacesResult(FacesResult facesResult) throws MessagingException;
}
