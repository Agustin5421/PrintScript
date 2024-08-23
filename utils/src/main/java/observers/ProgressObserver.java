package observers;

import printer.ProgressType;

public class ProgressObserver implements Observer {
  private final ProgressPrinter progressPrinter;
  private float totalProgress;
  private final int TOTAL_MODULES;

  public ProgressObserver(ProgressPrinter progressPrinter, int totalModules) {
    this.progressPrinter = progressPrinter;
    TOTAL_MODULES = totalModules;
  }

  public void finish() {
    progressPrinter.printProgress(100, ProgressType.COMPLETED);
  }

  public void error() {
    progressPrinter.printProgress(0, ProgressType.ERROR);
  }

  @Override
  public void update(Observable observable) {
    Progressable progressable = (Progressable) observable;
    float progress = progressable.getProgress();

    totalProgress += progress / TOTAL_MODULES;

    progressPrinter.printProgress(totalProgress, ProgressType.IN_PROGRESS);
  }
}
