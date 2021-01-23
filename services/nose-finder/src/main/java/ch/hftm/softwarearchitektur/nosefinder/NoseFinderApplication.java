package ch.hftm.softwarearchitektur.nosefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nu.pattern.OpenCV;

@SpringBootApplication
public class NoseFinderApplication {

    static {
		OpenCV.loadLocally();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(NoseFinderApplication.class, args);
	}

}
