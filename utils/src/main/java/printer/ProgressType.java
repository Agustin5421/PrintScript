package printer;

public enum ProgressType {
  IN_PROGRESS("\033[34m"), // Blue
  COMPLETED("\033[32m"), // Green
  ERROR("\033[31m"); // RED

  private final String colorCode;

  ProgressType(String colorCode) {
    this.colorCode = colorCode;
  }

  public String getColorCode() {
    return colorCode;
  }
}
