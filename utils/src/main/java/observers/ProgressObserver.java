package observers;

import printer.ProgressType;

public class ProgressObserver implements Observer {
  private final ProgressPrinter progressPrinter;
  private final int totalModules;

  public ProgressObserver(ProgressPrinter progressPrinter, int totalModules) {
    this.progressPrinter = progressPrinter;
    this.totalModules = totalModules;
  }

  public void finish() {
    progressPrinter.printProgress(100, ProgressType.COMPLETED);
  }

  public void error() {
    progressPrinter.printProgress(0, ProgressType.ERROR);
  }

  @Override
  public void update(Observable observable) {
    progressPrinter.printProgress(100 / totalModules, ProgressType.IN_PROGRESS);
  }
}
