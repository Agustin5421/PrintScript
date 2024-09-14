package interpreter.visitor.staticprovider;

import java.util.LinkedList;
import java.util.Queue;

public class Inputs {
  private static Queue<String> inputs = new LinkedList<>();

  public static void setInputs(Queue<String> inputs) {
    Inputs.inputs = inputs;
  }

  public static String nextInput() throws IllegalStateException {
    String input = inputs.poll();
    if (input == null) {
      throw new IllegalStateException("No more inputs available");
    }
    return input;
  }
}
