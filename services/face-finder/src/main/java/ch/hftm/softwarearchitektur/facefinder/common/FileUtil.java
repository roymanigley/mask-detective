package ch.hftm.softwarearchitektur.facefinder.common;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

@Log4j2
public class FileUtil {

    public static Path createTempFile(String suffix) throws IOException {
        return FileUtil.createTempFile("faces_", "." + suffix);
    }

    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, "." + suffix);
    }

    public static byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static void deleteSafe(Path ...paths) {
        Arrays.stream(paths).forEach(path -> {
            try {
                Files.delete(path);
            } catch (Exception e) {
                log.warn("could not delete: {}, {}", path, e.getMessage());
            }
        });
    }

    public static void write(Path path, byte[] imageBytes) throws IOException {
        Files.write(path, imageBytes, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }
}
