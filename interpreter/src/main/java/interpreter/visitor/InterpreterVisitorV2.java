package interpreter.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import env.EnvLoader;
import interpreter.VariablesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import token.Position;

public class InterpreterVisitorV2 implements InterpreterVisitor {

  private final InterpreterVisitor previousVisitor;
  private final VariablesRepository variablesRepository;
  private final Properties envProperties;
  private final List<String> printedValues;
  private final Literal<?> value;

  public InterpreterVisitorV2(
      InterpreterVisitor previousVisitor, VariablesRepository variablesRepository) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.printedValues = new ArrayList<>();
    this.envProperties = EnvLoader.loadEnvProperties();
    this.value = new NumberLiteral(0, new Position(0, 0), new Position(0, 0));
  }

  private InterpreterVisitorV2(
      InterpreterVisitor previousVisitor,
      VariablesRepository variablesRepository,
      List<String> printedValues) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.envProperties = EnvLoader.loadEnvProperties();
    this.printedValues = new ArrayList<>(printedValues);
    this.value = new NumberLiteral(0, new Position(0, 0), new Position(0, 0));
  }

  private InterpreterVisitorV2(
      InterpreterVisitor previousVisitor,
      VariablesRepository variablesRepository,
      List<String> printedValues,
      Literal<?> value) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.envProperties = EnvLoader.loadEnvProperties();
    this.printedValues = new ArrayList<>(printedValues);
    this.value = value;
  }

  public Literal<?> getValue() {
    return value;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    // Implementación específica para IfStatement
    return this;
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    // Implementación específica para BooleanLiteral
    return new InterpreterVisitorV2(
        previousVisitor, variablesRepository, printedValues, booleanLiteral);
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    List<AstNode> arguments = callExpression.arguments();
    Identifier identifier = callExpression.methodIdentifier();

    String name = identifier.name();

    if (name.equals("readEnv")) {
      return handleReadEnv(callExpression, arguments);
    } else if (name.equals("readInput")) {
      return handleReadInput(callExpression);
      //    } else if (name.equals("println")) {
      //      List<String> newPrintedValues = printlnMethod(identifier, arguments);
      //      return new InterpreterVisitorV2(previousVisitor, variablesRepository,
      // newPrintedValues);
    } else {
      return previousVisitor.visitCallExpression(callExpression);
    }
  }

  private NodeVisitor handleReadInput(CallExpression callExpression) {
    Scanner scanner = new Scanner(System.in);
    String userInput = "";

    while (userInput.isEmpty()) {
      System.out.println("Please enter input: ");
      userInput = scanner.nextLine();
      if (userInput.isEmpty()) {
        System.out.println("Input cannot be empty. Please enter a valid input.");
        System.out.println();
      }
    }

    System.out.println(userInput);
    List<String> newPrintedValues = new ArrayList<>(printedValues);
    newPrintedValues.add(userInput);

    Literal<?> result = getLiteral(callExpression, userInput);

    Identifier identifier = callExpression.methodIdentifier();
    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(identifier, result);
    return new InterpreterVisitorV2(previousVisitor, newVariablesRepository, newPrintedValues);
  }

  private static Literal<?> getLiteral(CallExpression callExpression, String userInput) {
    Literal<?> result;
    Position start = callExpression.start();
    Position end = callExpression.end();

    if (userInput.matches("-?\\d+(\\.\\d+)?([eE]-?\\d+)?")) {
      Number number;
      if (userInput.contains(".") || userInput.matches(".*[eE].*")) {
        number = Double.parseDouble(userInput);
      } else {
        number = Integer.parseInt(userInput);
      }
      result = new NumberLiteral(number, start, end);
    } else if (userInput.matches("(?i)^(true|false|t|f)$")) {
      boolean bool = userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("t");
      result = new BooleanLiteral(bool, start, end);
    } else {
      result = new StringLiteral(userInput, start, end);
    }
    return result;
  }

  private NodeVisitor handleReadEnv(CallExpression callExpression, List<AstNode> arguments) {
    if (arguments.size() != 1 || !(arguments.get(0) instanceof StringLiteral)) {
      throw new IllegalArgumentException("readEnv expects a single string argument");
    }

    String envVarName = ((StringLiteral) arguments.get(0)).value();
    String envVarValue =
        envProperties.getProperty(envVarName); // Obtener el valor de la variable de entorno

    if (envVarValue == null) {
      throw new IllegalArgumentException("Environment variable " + envVarName + " not found");
    }

    Literal<?> result = getLiteral(callExpression, envVarValue);

    Identifier identifier = callExpression.methodIdentifier();
    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(identifier, result);
    return new InterpreterVisitorV2(previousVisitor, newVariablesRepository, printedValues, result);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    NodeVisitor v1Result = previousVisitor.visitAssignmentExpression(assignmentExpression);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    NodeVisitor v1Result = previousVisitor.visitVarDec(variableDeclaration);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    NodeVisitor v1Result = previousVisitor.visitNumberLiteral(numberLiteral);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    NodeVisitor v1Result = previousVisitor.visitStringLiteral(stringLiteral);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    NodeVisitor v1Result = previousVisitor.visitIdentifier(identifier);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    NodeVisitor v1Result = previousVisitor.visitBinaryExpression(binaryExpression);
    return new InterpreterVisitorV2(
        (InterpreterVisitorV1) v1Result,
        ((InterpreterVisitorV1) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitorV1) v1Result).getValue());
  }

  @Override
  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  @Override
  public List<String> getPrintedValues() {
    return new ArrayList<>(printedValues);
  }

  @Override
  public InterpreterVisitor getPreviousVisitor() {
    return previousVisitor;
  }
}
