package ch.hftm.softwarearchitektur.nosefinder.publisher;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(NoseFinderSource.class)
public class NoseFinderPublisher {
}
