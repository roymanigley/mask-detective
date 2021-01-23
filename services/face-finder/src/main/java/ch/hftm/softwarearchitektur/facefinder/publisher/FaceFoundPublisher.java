package ch.hftm.softwarearchitektur.facefinder.publisher;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(FaceFoundSource.class)
public class FaceFoundPublisher {
}
