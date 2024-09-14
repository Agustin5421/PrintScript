package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.staticprovider.EnvLoader;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class InterpreterPrintStatementTest {
  private final List<String> prints = List.of();
  private final List<String> printedValues = List.of();
  // version 1.0
  @Test
  public void testArithOp() {
    String code = "let numberResult: number = 5 * 5 - 8; println(numberResult);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(17, repository.getNewVariable(new VariableIdentifier("numberResult")).value());
  }

  @Test
  public void testArithOpDec() {
    String code =
        """
                let pi: number;
                pi = 3.14;
                println(pi / 2);
                """;
    Interpreter interpreter = new Interpreter("1.0");
//    List<String> printedValues = interpreter.interpret(code);
    assertEquals(1.57, Double.parseDouble(printedValues.get(0)));
  }

  @Test
  public void testDeclareAssign() {
    String code =
        """
                let result: number;
                                result = 5;
                                println(result);
                """;
    Interpreter interpreter = new Interpreter("1.0");
//    List<String> printedValues = interpreter.interpret(code);
    assertEquals(5, Double.parseDouble(printedValues.get(0)));
  }

  @Test
  public void testStrConcatNumb() {
    String code =
        """
                let someNumber: number = 1;
                let someString: string = "hello world ";
                println(someString + someNumber);
                """;
    Interpreter interpreter = new Interpreter("1.0");
//    List<String> printedValues = interpreter.interpret(code);
    assertEquals("hello world 1", printedValues.get(0));
  }

  // version 1.1
  @Test
  public void testArithOp2() {
    String code = "let numberResult: number = 5 * 5 - 8; println(numberResult);";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(17, repository.getNewVariable(new VariableIdentifier("numberResult")).value());
  }

  @Test
  public void testArithOpDec2() {
    String code =
        """
                let pi: number;
                pi = 3.14;
                println(pi / 2);
                """;
    Interpreter interpreter = new Interpreter("1.1");
//    List<String> printedValues = interpreter.interpret(code);
    assertEquals(1.57, Double.parseDouble(printedValues.get(0)));
  }

  @Test
  public void testElseFalse() {
    String code =
        """
                const booleanResult: boolean = false;
                if(booleanResult) {
                    println("else statement not working correctly");
                } else {
                    println("else statement working correctly");
                }
                println("outside of conditional");
                """;
    Interpreter interpreter = new Interpreter("1.1");
//    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("else statement working correctly", "outside of conditional");
    assertEquals(expected, prints);
  }

  @Test
  public void testElseTrue() {
    String code =
        """
                const booleanResult: boolean = true;
                if(booleanResult) {
                    println("else statement working correctly");
                } else {
                    println("else statement not working correctly");
                }
                println("outside of conditional");
                """;
    Interpreter interpreter = new Interpreter("1.1");
//    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("else statement working correctly", "outside of conditional");
    assertEquals(expected, prints);
  }

  @Test
  public void testIfTrue() {
    String code =
        """
                const booleanValue: boolean = false;
                if(booleanValue) {
                    println("if statement is not working correctly");
                }
                println("outside of conditional");
                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("outside of conditional");
    assertEquals(expected, prints);
  }

  @Test
  public void testIfFalse() {
    String code =
        """
                const booleanValue: boolean = true;
                                                  if(booleanValue) {
                                                      println("if statement working correctly");
                                                  }
                                                  println("outside of conditional");

                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("if statement working correctly", "outside of conditional");
    assertEquals(expected, prints);
  }

  @Test
  public void testReadEnvExistingVariable() {
    EnvLoader.addNewConstants("BEST_FOOTBALL_CLUB", "San Lorenzo");
    String code =
        """
                const name: string = readEnv("BEST_FOOTBALL_CLUB");
                println("What is the best football club?");
                println(name);
                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("What is the best football club?", "San Lorenzo");
    assertEquals(expected, prints);
  }

  @Test
  public void testReadInput() {
    String input = "world";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code =
        """
                const name: string = readInput("Name:");
                println("Hello " + name + "!");
                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> prints = interpreter.interpret(code);
    List<String> expected = List.of("Name:", "Hello world!");
    assertEquals(expected, prints);
  }

  @Test
  public void testDeclareAssign2() {
    String code =
        """
                let result: number;
                                result = 5;
                                println(result);
                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);
    assertEquals(5, Double.parseDouble(printedValues.get(0)));
  }

  @Test
  public void testStrConcatNumb2() {
    String code =
        """
                let someNumber: number = 1;
                let someString: string = "hello world ";
                println(someString + someNumber);
                """;
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);
    assertEquals("hello world 1", printedValues.get(0));
  }
}
