package interpreter.visitor;

public class VersionManager {
  private static String currentVersion = "1.1";

  public static String getCurrentVersion() {
    return currentVersion;
  }

  public static void setCurrentVersion(String version) {
    currentVersion = version;
  }
}
