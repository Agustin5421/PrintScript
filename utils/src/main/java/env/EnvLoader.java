package env;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {

  private static final String ENV_FILE_PATH = "../.env";

  public static Properties loadEnvProperties() {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(ENV_FILE_PATH)) {
      properties.load(fis);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }
}