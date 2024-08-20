package observers;

import printer.ProgressType;

public class ProgressObserver implements Observer{
  private final ProgressPrinter progressPrinter;
  private float totalProgress;
  private static final int TOTAL_MODULES = 3;
  private static final int PERCENTAGE_PER_MODULE = 100 / TOTAL_MODULES;

  public ProgressObserver(ProgressPrinter progressPrinter) {
    this.progressPrinter = progressPrinter;
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
    float progress =  progressable.getProgress();

    totalProgress += progress / TOTAL_MODULES;

    progressPrinter.printProgress(totalProgress, ProgressType.IN_PROGRESS);

  }
}
