package input;

import java.util.Queue;

public class InputQueue implements InputHandler {
  private final Queue<String> inputs;

  public InputQueue(Queue<String> inputs) {
    this.inputs = inputs;
  }

  @Override
  public String getInput() {
    String inputToReturn = inputs.poll();

    if (inputToReturn == null) {
      throw new IllegalStateException("No more inputs available");
    }

    return inputToReturn;
  }
}
