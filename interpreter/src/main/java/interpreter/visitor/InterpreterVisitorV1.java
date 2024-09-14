package interpreter.visitor;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;

import java.util.List;

import output.OutputResult;


public class InterpreterVisitorV1 implements InterpreterVisitor {
  public InterpreterVisitorV1(VariablesRepository variablesRepository, List<String> printedValues) {

  }

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

  @Override
  public OutputResult<?> getOutputResult() {
    return null;
  }
  /*
  private final VariablesRepository variablesRepository;
  private final Literal<?> value;
  private final BinaryExpressionEvaluator binaryExpressionEvaluator =
      new BinaryExpressionEvaluator();
  private final List<String> printedValues;
  private final LiteralHandler literalHandler;

  public InterpreterVisitorV1(VariablesRepository variablesRepository, Literal<?> value) {
    this.variablesRepository = variablesRepository;
    this.value = value;
    this.printedValues = new ArrayList<>();
    this.literalHandler = new LiteralHandler();
  }

  public InterpreterVisitorV1(VariablesRepository variablesRepository) {
    this(variablesRepository, new NumberLiteral(0, new Position(0, 0), new Position(0, 0)));
  }

  private InterpreterVisitorV1(
      VariablesRepository variablesRepository, Literal<?> value, List<String> printedValues) {
    this.variablesRepository = variablesRepository;
    this.value = value;
    this.printedValues = new ArrayList<>(printedValues);
    this.literalHandler = new LiteralHandler();
  }

  public InterpreterVisitorV1(VariablesRepository variablesRepository, List<String> printedValues) {
    this.variablesRepository = variablesRepository;
    this.value = new NumberLiteral(0, new Position(0, 0), new Position(0, 0));
    this.printedValues = new ArrayList<>(printedValues);
    this.literalHandler = new LiteralHandler();
  }

  @Override
  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  public Literal<?> getValue() {
    return value;
  }

  @Override
  public InterpreterVisitor cloneVisitor() {
    return new InterpreterVisitorV1(variablesRepository, value, new ArrayList<>());
  }

  @Override
  public NodeVisitor visit(AstNode node) {
    return this;
  }

  @Override
  public OutputResult<?> getOutputResult() {
    return null;
  }

  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    throw new IllegalArgumentException("If Node not supported in this version :( ");
  }

  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    throw new IllegalArgumentException("Boolean Node not supported in this version :( ");
  }

  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    List<AstNode> arguments = callExpression.arguments();
    Identifier identifier = callExpression.methodIdentifier();

    String name = identifier.name();

    if (name.equals("println")) {
      List<String> newPrintedValues = literalHandler.printlnMethod(arguments, printedValues, this);
      return new InterpreterVisitorV1(variablesRepository, value, newPrintedValues);
    } else {
      throw new IllegalArgumentException(name + " not supported in this version :( ");
    }
  }

  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(left);

    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitorWithParams(
            variablesRepository, printedValues);
    
    Literal<?> evaluatedRight =
        ((InterpreterVisitor) assignmentExpression.right().accept(latestVisitor)).getValue();

    VariablesRepository newVariablesRepository =
        variablesRepository.setNewVariable(varId, evaluatedRight);

    return new InterpreterVisitorV1(newVariablesRepository, evaluatedRight, printedValues);
  }

  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromVarDec(variableDeclaration);

    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitorWithParams(
            variablesRepository, printedValues);

    ExpressionNode expression = variableDeclaration.expression();

    List<String> printedList;
    InterpreterVisitor visitor;
    Literal<?> value;
    if (expression == null) {
      value = null;
      printedList = printedValues;
    } else {
      visitor = ((InterpreterVisitor) expression.accept(latestVisitor));
      value = visitor.getValue();
      printedList = visitor.getPrintedValues();
    }

    VariablesRepository newVariablesRepository =
        getVariablesRepository().addNewVariable(varId, value);

    return new InterpreterVisitorV1(newVariablesRepository, value, printedList);
  }

  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new InterpreterVisitorV1(variablesRepository, numberLiteral, printedValues);
  }

  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new InterpreterVisitorV1(variablesRepository, stringLiteral, printedValues);
  }

  public NodeVisitor visitIdentifier(Identifier identifier) {
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    Literal<Object> value = getVariablesRepository().getNewVariable(varId);

    return new InterpreterVisitorV1(getVariablesRepository(), value);
  }

  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitorWithParams(
            variablesRepository, printedValues);
    Literal<?> value = binaryExpressionEvaluator.evaluate(binaryExpression, latestVisitor);
    return new InterpreterVisitorV1(variablesRepository, value, printedValues);
  }

  public List<String> getPrintedValues() {
    return new ArrayList<>(printedValues);
  }

  public InterpreterVisitor getPreviousVisitor() {
    return null;
  }
  
   */
}
