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
import interpreter.VariablesRepository;

import java.util.List;
import java.util.Scanner;

public class InterpreterVisitorV2 implements NodeVisitor {

  private final InterpreterVisitorV1 interpreterVisitorV1;
  private final VariablesRepository variablesRepository;

  public InterpreterVisitorV2(InterpreterVisitorV1 interpreterVisitorV1, VariablesRepository variablesRepository) {
    this.interpreterVisitorV1 = interpreterVisitorV1;
      this.variablesRepository = variablesRepository;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    // Implementación específica para IfStatement
    return this;
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    // Implementación específica para BooleanLiteral
    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    List<AstNode> arguments = callExpression.arguments();
    Identifier identifier = callExpression.methodIdentifier();
    boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

    String name = identifier.name();

    if (name.equals("readEnv")) {
      return readEnvMethod(identifier, arguments);
    } else if (name.equals("readInput")) {
      return handleReadInput(callExpression);
    } else {
      return interpreterVisitorV1.visitCallExpression(callExpression);
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

    Literal<?> result = getLiteral(callExpression, userInput);

    Identifier identifier = callExpression.methodIdentifier();
    VariablesRepository newVariablesRepository = variablesRepository.addVariable(identifier, result);
    return new InterpreterVisitorV2(interpreterVisitorV1, newVariablesRepository);
  }

  private static Literal<?> getLiteral(CallExpression callExpression, String userInput) {
    Literal<?> result;
    if (userInput.matches("-?\\d+(\\.\\d+)?")) {
        Number number;
        if (userInput.contains(".")) {
            number = Double.parseDouble(userInput);
        } else {
            number = Integer.parseInt(userInput);
        }
        result = new NumberLiteral(number, callExpression.start(), callExpression.end());
    } else if (userInput.matches("(?i)^(true|false|t|f)$")) {
      boolean bool = userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("t");
        result = new BooleanLiteral(bool, callExpression.start(), callExpression.end());
    } else {
        result = new StringLiteral(userInput, callExpression.start(), callExpression.end());
    }
    return result;
  }

  private NodeVisitor readEnvMethod(Identifier identifier, List<AstNode> arguments) {
    if (arguments.size() != 1 || !(arguments.get(0) instanceof StringLiteral)) {
      throw new IllegalArgumentException("readEnv expects a single string argument");
    }

    String envVarName = ((StringLiteral) arguments.get(0)).value();
    String envVarValue = System.getenv(envVarName);

    if (envVarValue == null) {
      throw new IllegalArgumentException("Environment variable " + envVarName + " not found");
    }

    Literal<?> result;
    try {
      result = new StringLiteral(envVarValue, identifier.start(), identifier.end());
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert environment variable " + envVarName + " to the expected type");
    }

    return this;
  }


  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    return interpreterVisitorV1.visitAssignmentExpression(assignmentExpression);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return interpreterVisitorV1.visitVarDec(variableDeclaration);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return interpreterVisitorV1.visitNumberLiteral(numberLiteral);
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return interpreterVisitorV1.visitStringLiteral(stringLiteral);
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return interpreterVisitorV1.visitIdentifier(identifier);
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return interpreterVisitorV1.visitBinaryExpression(binaryExpression);
  }

  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }
}
