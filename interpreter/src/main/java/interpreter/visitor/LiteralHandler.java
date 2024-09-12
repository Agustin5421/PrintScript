package interpreter.visitor;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.visitor.env.EnvLoader;
import interpreter.visitor.patternStat.*;
import java.util.*;
import token.Position;

public class LiteralHandler {
  private static final Map<String, LiteralStrategy> strategies = new LinkedHashMap<>();

  static {
    strategies.put("(?i)^(true|false|t|f)$", new BooleanLiteralStrategy());
    strategies.put("-?\\d+(\\.\\d+)?([eE]-?\\d+)?", new NumberLiteralStrategy());
    strategies.put(".*", new StringLiteralStrategy());
  }

  public ResultLiteral handleReadInput(
      CallExpression callExpression, List<String> printedValues, String userInput) {
    Scanner scanner = new Scanner(System.in);

    int argumentSize = callExpression.arguments().size();
    if (argumentSize != 1) {
      throw new IllegalArgumentException("readInput expects a single string argument");
    }

    String input = ((StringLiteral) callExpression.arguments().get(0)).value();
    while (userInput.isEmpty()) {

      System.out.println(input);
      userInput = scanner.nextLine();
      if (userInput.isEmpty()) {
        System.out.println("Input cannot be empty. Please enter a valid input.");
        System.out.println();
      }
    }

    System.out.println(userInput);
    printedValues.add(userInput);

    return new ResultLiteral(getLiteral(callExpression, userInput), printedValues);
  }

  public Literal<?> handleReadEnv(CallExpression callExpression, List<AstNode> arguments) {
    if (arguments.size() != 1 || !(arguments.get(0) instanceof StringLiteral)) {
      throw new IllegalArgumentException("readEnv expects a single string argument");
    }

    String envVarName = ((StringLiteral) arguments.get(0)).value();
    String envVarValue = EnvLoader.getValue(envVarName);

    if (envVarValue == null) {
      throw new IllegalArgumentException("Environment variable " + envVarName + " not found");
    }

    return getLiteral(callExpression, envVarValue);
  }

  public static Literal<?> getLiteral(CallExpression callExpression, String userInput) {
    Position start = callExpression.start();
    Position end = callExpression.end();

    for (Map.Entry<String, LiteralStrategy> entry : strategies.entrySet()) {
      if (userInput.matches(entry.getKey())) {
        return entry.getValue().createLiteral(userInput, start, end);
      }
    }
    throw new IllegalArgumentException("No matching strategy found for input: " + userInput);
  }

  public List<String> printlnMethod(
      List<AstNode> arguments, List<String> printedValues, InterpreterVisitor visitor) {
    List<String> newPrintedValues = new ArrayList<>(printedValues);
    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitorWithParams(
            visitor.getVariablesRepository(), printedValues);

    for (AstNode argument : arguments) {
      String value =
          ((InterpreterVisitor) argument.accept(latestVisitor)).getValue().value().toString();
      System.out.println(value);
      newPrintedValues.add(value);
    }
    System.out.println();
    return newPrintedValues;
  }
}
