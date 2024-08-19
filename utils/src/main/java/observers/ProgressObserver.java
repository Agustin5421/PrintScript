package observers;

import java.util.HashMap;
import java.util.Map;
import printer.ProgressType;

public class ProgressObserver {
  private final ProgressPrinter progressPrinter;
  private final Map<String, Integer> moduleProgress;
  private final Map<String, Integer> modulePercentages;

  public ProgressObserver(ProgressPrinter progressPrinter) {
    this.progressPrinter = progressPrinter;
    this.moduleProgress = new HashMap<>();
    this.modulePercentages = new HashMap<>();
  }

  public void registerModule(String moduleName, int percentage) {
    modulePercentages.put(moduleName, percentage);
    moduleProgress.put(moduleName, 0);
  }

  public void update(String moduleName, int progress) {
    int percentage = modulePercentages.getOrDefault(moduleName, 0);
    int progressValue = (progress * percentage) / 100;
    moduleProgress.put(moduleName, progressValue);

    int totalProgress = moduleProgress.values().stream().mapToInt(Integer::intValue).sum();
    int totalPercentage =
        Math.min(totalProgress, 100); // Ensure total percentage does not exceed 100%

    progressPrinter.printProgress(totalPercentage, ProgressType.IN_PROGRESS);
  }

  public void finish() {
    progressPrinter.printProgress(100, ProgressType.COMPLETED);
  }

  public void error(int progress) {
    progressPrinter.printProgress(progress, ProgressType.ERROR);
  }
}
