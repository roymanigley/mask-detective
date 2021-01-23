package ch.hftm.softwarearchitektur.facefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nu.pattern.OpenCV;

@SpringBootApplication
public class FaceFinderApplication {

    static {
		OpenCV.loadLocally();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FaceFinderApplication.class, args);
	}

}
