package org.competitions.kickstart.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class FileUtils {

    public static String readResourceContents(String resourceFilename) throws IOException {
        try (var inputStream = getResourceAsStream(resourceFilename)) {
            Objects.requireNonNull(inputStream);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static InputStream getResourceAsStream(String resourceFilename) {
        return FileUtils.class.getClassLoader().getResourceAsStream(resourceFilename);
    }
}
