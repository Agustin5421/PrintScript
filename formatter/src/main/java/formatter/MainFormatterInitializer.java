package formatter;

import java.util.List;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;

public class MainFormatterInitializer {
  public static MainFormatter init() {
    return init(new ProgressObserver(new ProgressPrinter(), 1));
  }

  public static MainFormatter init(Observer observer) {
    return new MainFormatter(List.of(observer));
  }
}
