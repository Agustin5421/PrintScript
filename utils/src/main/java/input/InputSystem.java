package input;

import java.util.Scanner;

public class InputSystem implements InputHandler {
  @Override
  public String getInput() {
    System.out.println();

    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter input value: ");
    String input = scanner.nextLine();

    //    scanner.close();

    return input;
  }
}
