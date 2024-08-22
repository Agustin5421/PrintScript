package linter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static String readResourceFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)));
        } catch (IOException e) {
            return null;
        }
    }
}
