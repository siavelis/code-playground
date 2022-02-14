package org.prs.hashcode.onepizza.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class DataLoader {

    public static String loadData(String resourceName) throws IOException {
        try (var inputStream = DataLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceName)) {
            Objects.requireNonNull(inputStream);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
