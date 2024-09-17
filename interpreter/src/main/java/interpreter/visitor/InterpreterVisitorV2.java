package interpreter.visitor;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import java.util.*;

public class InterpreterVisitorV2 implements InterpreterVisitor {
  public InterpreterVisitorV2(
      InterpreterVisitorV1 interpreterVisitorV1,
      VariablesRepository variablesRepository,
      List<String> printedValues) {}

  @Override
  public VariablesRepository getVariablesRepository() {
    return null;
  }

  @Override
  public InterpreterVisitor cloneVisitor() {
    return null;
  }

  @Override
  public NodeVisitor visit(AstNode node) {
    return null;
  }

  /*
  private final InterpreterVisitor previousVisitor;
  private final VariablesRepository variablesRepository;
  private final List<String> printedValues;
  private final LiteralHandler literalHandler;
  private final Literal<?> value;

  public InterpreterVisitorV2(
      InterpreterVisitor previousVisitor, VariablesRepository variablesRepository) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.printedValues = new ArrayList<>();
    this.value = new NumberLiteral(0, new Position(0, 0), new Position(0, 0));
    this.literalHandler = new LiteralHandler();
  }

  public InterpreterVisitorV2(
      InterpreterVisitor previousVisitor,
      VariablesRepository variablesRepository,
      List<String> printedValues) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.printedValues = new ArrayList<>(printedValues);
    this.value = new NumberLiteral(0, new Position(0, 0), new Position(0, 0));
    this.literalHandler = new LiteralHandler();
  }

  public InterpreterVisitorV2(
      InterpreterVisitor previousVisitor,
      VariablesRepository variablesRepository,
      List<String> printedValues,
      Literal<?> value) {
    this.previousVisitor = previousVisitor;
    this.variablesRepository = variablesRepository;
    this.printedValues = new ArrayList<>(printedValues);
    this.value = value;
    this.literalHandler = new LiteralHandler();
  }

  public Literal<?> getValue() {
    return value;
  }

  @Override
  public InterpreterVisitor cloneVisitor() {
    return new InterpreterVisitorV2(
        previousVisitor.cloneVisitor(), variablesRepository, new ArrayList<>(), value);
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {

    Literal<?> conditionValue =
        ((InterpreterVisitor) ifStatement.getCondition().accept(this)).getValue();
    boolean condition = ((BooleanLiteral) conditionValue).value();

    InterpreterVisitor nestedVisitor = this;

    if (condition) {
      for (AstNode sta : ifStatement.getThenBlockStatement()) {
        nestedVisitor = (InterpreterVisitor) sta.accept(nestedVisitor);
      }
    } else if (ifStatement.getElseBlockStatement() != null) {
      for (AstNode sta : ifStatement.getElseBlockStatement()) {
        nestedVisitor = (InterpreterVisitor) sta.accept(nestedVisitor);
      }
    }

    VariablesRepository nestedVisitorVariablesRepository = nestedVisitor.getVariablesRepository();
    VariablesRepository updatedVarRepo =
        variablesRepository.update(nestedVisitorVariablesRepository);
    InterpreterVisitor updatedPrevVisitor = new InterpreterVisitorV1(updatedVarRepo);

    List<String> nestedPrints = nestedVisitor.getPrintedValues();
    List<String> updatedPrints = new ArrayList<>(printedValues);
    updatedPrints.addAll(nestedPrints);
    return new InterpreterVisitorV2(
        updatedPrevVisitor, updatedVarRepo, updatedPrints, nestedVisitor.getValue());
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
    } else {
      InterpreterVisitor innerVisitor =
          (InterpreterVisitor) previousVisitor.visitCallExpression(callExpression);
      List<String> nestedPrints = innerVisitor.getPrintedValues();
      List<String> updatedPrints = new ArrayList<>(printedValues);
      for (String print : nestedPrints) {
        if (!printedValues.contains(print)) {
          updatedPrints.add(print);
        }
      }

      return new InterpreterVisitorV2(
          innerVisitor, variablesRepository, updatedPrints, innerVisitor.getValue());
    }
  }

  private NodeVisitor handleReadInput(CallExpression callExpression) {
    String savedInputs = Inputs.getInputs().poll();
    // Check if there is a user input saved in the queue or else it will
    // wait for the user to input a value
    String userInput = savedInputs == null ? "" : savedInputs;
    ResultLiteral result = literalHandler.handleReadInput(callExpression, printedValues, userInput);
    Literal<?> resultLiteral = result.literal();
    List<String> newPrintedValues = result.strings();

    Identifier identifier = callExpression.methodIdentifier();
    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(identifier, resultLiteral);
    return new InterpreterVisitorV2(
        previousVisitor, newVariablesRepository, newPrintedValues, resultLiteral);
  }

  private NodeVisitor handleReadEnv(CallExpression callExpression, List<AstNode> arguments) {
    Literal<?> result = literalHandler.handleReadEnv(callExpression, arguments);
    Identifier identifier = callExpression.methodIdentifier();
    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(identifier, result);
    return new InterpreterVisitorV2(previousVisitor, newVariablesRepository, printedValues, result);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    NodeVisitor v1Result = previousVisitor.visitAssignmentExpression(assignmentExpression);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitor) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    NodeVisitor v1Result = previousVisitor.visitVarDec(variableDeclaration);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        ((InterpreterVisitor) v1Result).getPrintedValues(),
        ((InterpreterVisitor) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    NodeVisitor v1Result = previousVisitor.visitNumberLiteral(numberLiteral);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitor) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    NodeVisitor v1Result = previousVisitor.visitStringLiteral(stringLiteral);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitor) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    NodeVisitor v1Result = previousVisitor.visitIdentifier(identifier);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitor) v1Result).getValue());
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    NodeVisitor v1Result = previousVisitor.visitBinaryExpression(binaryExpression);
    return new InterpreterVisitorV2(
        (InterpreterVisitor) v1Result,
        ((InterpreterVisitor) v1Result).getVariablesRepository(),
        printedValues,
        ((InterpreterVisitor) v1Result).getValue());
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

   */
}
