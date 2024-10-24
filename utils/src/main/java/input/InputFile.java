package input;

import java.util.List;

public class InputFile implements InputHandler {
  private List<String> inputs;

  public InputFile(List<String> inputs) {
    this.inputs = inputs;
  }

  @Override
  public String getInput() {
    return inputs.remove(0);
  }
}
