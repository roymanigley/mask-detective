package ch.hftm.softwarearchitektur.nosefinder.ai.classifier.impl;

import ch.hftm.softwarearchitektur.nosefinder.ai.classifier.NoseFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.nosefinder.domain.Nose;
import ch.hftm.softwarearchitektur.nosefinder.service.impl.OpenCvResultMapper;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class OpenCvNoseFinderClassifierAdapter implements NoseFinderClassifierAdapter {

    private final CascadeClassifier classifier;

    public OpenCvNoseFinderClassifierAdapter(CascadeClassifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public Set<Nose> detectNoses(Path tempFile) {
        Mat loadedImage = loadedImage(tempFile);
        MatOfRect noseDetected = new MatOfRect();
        int minNoseSize = Math.round(loadedImage.rows() * 0.1f);
        double scaleFactor = 1.1;
        int minNeighbors = 3;
        classifier.detectMultiScale(loadedImage, noseDetected, scaleFactor, minNeighbors,
                Objdetect.CASCADE_SCALE_IMAGE, new Size(minNoseSize, minNoseSize), new Size());
        return Arrays.stream(noseDetected.toArray()).map(OpenCvResultMapper::toNose).collect(Collectors.toSet());
    }

    private Mat loadedImage(Path imagePath) {
        return Imgcodecs.imread(imagePath.toString());
    }
}
