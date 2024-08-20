package observers;

import printer.ProgressType;

public class ProgressPrinter {
  private static final int BAR_WIDTH = 50;

  public void printProgress(float progress, ProgressType type) {
    float completedWidth = (progress * BAR_WIDTH) / 100;
    StringBuilder sb = new StringBuilder();
    String color = type.getColorCode();

    sb.append(color);
    sb.append("[");
    for (int i = 0; i < BAR_WIDTH; i++) {
      if (i < completedWidth) {
        sb.append("=");
      } else {
        sb.append(" ");
      }
    }
    sb.append("] ").append(progress).append("%\033[0m\r");
    System.out.print(sb);
  }
}
