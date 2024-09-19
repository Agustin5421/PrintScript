package observers;

import printer.ProgressType;

public class ProgressObserver implements Observer {
  private final ProgressPrinter progressPrinter;
  private float progress;

  public ProgressObserver(ProgressPrinter progressPrinter) {
    this.progressPrinter = progressPrinter;
  }

  public void finish() {
    progressPrinter.printProgress(100, ProgressType.COMPLETED);
  }

  public void error() {
    progressPrinter.printProgress((int) progress, ProgressType.ERROR);
  }

  @Override
  public void update(Observable observable) {
    progress = progress + observable.getProgress();
    progressPrinter.printProgress((int) progress, ProgressType.IN_PROGRESS);
  }
}
