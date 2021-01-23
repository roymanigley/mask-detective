package ch.hftm.softwarearchitektur.mouthfinder.ai.classifier.impl;

import ch.hftm.softwarearchitektur.mouthfinder.ai.classifier.MouthFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.mouthfinder.domain.Mouth;
import ch.hftm.softwarearchitektur.mouthfinder.service.impl.OpenCvResultMapper;
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

public class OpenCvMouthFinderClassifierAdapter implements MouthFinderClassifierAdapter {

    private final CascadeClassifier classifier;

    public OpenCvMouthFinderClassifierAdapter(CascadeClassifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public Set<Mouth> detectMouths(Path tempFile) {
        Mat loadedImage = loadedImage(tempFile);
        MatOfRect mouthDetected = new MatOfRect();
        int minMouthSize = Math.round(loadedImage.rows() * 0.1f);
        double scaleFactor = 1.1;
        int minNeighbors = 3;
        classifier.detectMultiScale(loadedImage, mouthDetected, scaleFactor, minNeighbors,
                Objdetect.CASCADE_SCALE_IMAGE, new Size(minMouthSize, minMouthSize), new Size());
        return Arrays.stream(mouthDetected.toArray()).map(OpenCvResultMapper::toMouth).collect(Collectors.toSet());
    }

    private Mat loadedImage(Path imagePath) {
        return Imgcodecs.imread(imagePath.toString());
    }
}
