package ch.hftm.softwarearchitektur.facefinder.ai.classifier.impl;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import ch.hftm.softwarearchitektur.facefinder.ai.classifier.FaceFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.facefinder.domain.Face;
import ch.hftm.softwarearchitektur.facefinder.service.impl.OpenCvResultMapper;

public class OpenCvFaceFinderClassifierAdapter implements FaceFinderClassifierAdapter {

    private final CascadeClassifier classifier;

    public OpenCvFaceFinderClassifierAdapter(CascadeClassifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public Set<Face> detectFaces(Path tempFile) {
        Mat loadedImage = loadedImage(tempFile);
        MatOfRect facesDetected = new MatOfRect();
        int minFaceSize = Math.round(loadedImage.rows() * 0.1f);
        double scaleFactor = 1.1;
        int minNeighbors = 3;
        classifier.detectMultiScale(loadedImage, facesDetected, scaleFactor, minNeighbors,
                Objdetect.CASCADE_SCALE_IMAGE, new Size(minFaceSize, minFaceSize), new Size());
        return Arrays.stream(facesDetected.toArray()).map(OpenCvResultMapper::toFace).collect(Collectors.toSet());
    }

    private Mat loadedImage(Path imagePath) {
        return Imgcodecs.imread(imagePath.toString());
    }
}
