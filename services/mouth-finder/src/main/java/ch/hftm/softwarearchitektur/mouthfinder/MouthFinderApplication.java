package ch.hftm.softwarearchitektur.mouthfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import nu.pattern.OpenCV;

@SpringBootApplication
public class MouthFinderApplication {

    static {
		OpenCV.loadLocally();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MouthFinderApplication.class, args);
	}

}
