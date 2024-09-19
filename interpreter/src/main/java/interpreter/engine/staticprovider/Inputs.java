package interpreter.engine.staticprovider;

import input.InputHandler;

public class Inputs {
  private static InputHandler inputs;

  public static void setInputs(InputHandler inputs) {
    Inputs.inputs = inputs;
  }

  public static String nextInput() throws IllegalStateException {
    String input = inputs.getInput();
    if (input == null) {
      throw new IllegalStateException("No more inputs available");
    }
    return input;
  }
}
