package ch.hftm.softwarearchitektur.facefinder.ai.conf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import ch.hftm.softwarearchitektur.facefinder.common.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import ch.hftm.softwarearchitektur.facefinder.ai.classifier.FaceFinderClassifierAdapter;
import ch.hftm.softwarearchitektur.facefinder.ai.classifier.impl.OpenCvFaceFinderClassifierAdapter;
import org.springframework.util.StreamUtils;

@Configuration
@Log4j2
public class ClassifierAdapterConf {

    @Value("${application.classifier.file}")
    private String customClassifier;

    @Bean
    public FaceFinderClassifierAdapter getClassifier() {
        final Path path = initClassifierFile();
        CascadeClassifier cascadeClassifier = new CascadeClassifier();
        log.info("CascadeClassifier: {}", path);
        cascadeClassifier.load(path.toString());
        return new OpenCvFaceFinderClassifierAdapter(cascadeClassifier);
    }

    private Path initClassifierFile() {
        return Optional.ofNullable(customClassifier)
                .map(Paths::get)
                .filter(Files::exists)
                .filter(Files::isRegularFile)
                .orElseGet(this::initDefaultClassifier);
    }

    private Path initDefaultClassifier() {
        try (final InputStream inputStream = new ClassPathResource("classifiers/haarcascade_frontalface_alt.xml").getInputStream();) {
            final Path tempFile = FileUtil.createTempFile("haarcascade_frontalface_alt", ".xml");
            final byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            FileUtil.write(tempFile, bytes);
            return tempFile;
        } catch (IOException e) {
            log.error("initDefaultClassifier failed ", e);
            throw new RuntimeException(e);
        }
    }
}