package interpreter.visitor;

import ast.expressions.BinaryExpression;
import ast.expressions.ExpressionNode;
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
import interpreter.visitor.evaluator.BinaryExpressionEvaluator;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import java.util.ArrayList;
import java.util.List;
import token.Position;

public class InterpreterVisitorV1 implements InterpreterVisitor { // }, NodeVisitor2 {
  private final VariablesRepository variablesRepository;
  private final Literal<?> value;
  private final BinaryExpressionEvaluator binaryExpressionEvaluator =
      new BinaryExpressionEvaluator();
  private final List<String> printedValues;

  public InterpreterVisitorV1(VariablesRepository variablesRepository, Literal<?> value) {
    this.variablesRepository = variablesRepository;
    this.value = value;
    this.printedValues = new ArrayList<>();
  }

  public InterpreterVisitorV1(VariablesRepository variablesRepository) {
    this(variablesRepository, new NumberLiteral(0, new Position(0, 0), new Position(0, 0)));
  }

  private InterpreterVisitorV1(
      VariablesRepository variablesRepository, Literal<?> value, List<String> printedValues) {
    this.variablesRepository = variablesRepository;
    this.value = value;
    this.printedValues = new ArrayList<>(printedValues);
  }

  @Override
  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  public Literal<?> getValue() {
    return value;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    throw new IllegalArgumentException("If Node not supported in this version :( ");
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    throw new IllegalArgumentException("Boolean Node not supported in this version :( ");
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    List<AstNode> arguments = callExpression.arguments();
    Identifier identifier = callExpression.methodIdentifier();

    String name = identifier.name();

    if (name.equals("println")) {
      List<String> newPrintedValues = printlnMethod(identifier, arguments);
      return new InterpreterVisitorV1(variablesRepository, value, newPrintedValues);
    } else {
      throw new IllegalArgumentException(name + " not supported in this version :( ");
    }
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(left);

    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitor(variablesRepository);

    Literal<?> evaluatedRight =
        ((InterpreterVisitor) assignmentExpression.right().accept(latestVisitor)).getValue();

    VariablesRepository newVariablesRepository =
        variablesRepository.setNewVariable(varId, evaluatedRight);

    return new InterpreterVisitorV1(newVariablesRepository, evaluatedRight, printedValues);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromVarDec(variableDeclaration);

    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitor(variablesRepository);

    ExpressionNode expression = variableDeclaration.expression();

    if (expression == null) {
      VariablesRepository newVariablesRepository =
          getVariablesRepository().addNewVariable(varId, null);
      return new InterpreterVisitorV1(newVariablesRepository, this.value, printedValues);
    }

    Literal<?> value = ((InterpreterVisitor) expression.accept(latestVisitor)).getValue();

    VariablesRepository newVariablesRepository =
        getVariablesRepository().addNewVariable(varId, value);

    return new InterpreterVisitorV1(newVariablesRepository, value, printedValues);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new InterpreterVisitorV1(variablesRepository, numberLiteral, printedValues);
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new InterpreterVisitorV1(variablesRepository, stringLiteral, printedValues);
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    Literal<Object> value = getVariablesRepository().getNewVariable(varId);

    return new InterpreterVisitorV1(getVariablesRepository(), value);
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitor(variablesRepository);
    Literal<?> value = binaryExpressionEvaluator.evaluate(binaryExpression, latestVisitor);
    return new InterpreterVisitorV1(variablesRepository, value, printedValues);
  }

  private List<String> printlnMethod(Identifier identifier, List<AstNode> arguments) {
    List<String> newPrintedValues = new ArrayList<>(printedValues);
    InterpreterVisitor latestVisitor =
        InterpreterVisitorFactory.getInterpreterVisitor(variablesRepository);
    for (AstNode argument : arguments) {
      String value =
          ((InterpreterVisitor) argument.accept(latestVisitor)).getValue().value().toString();
      System.out.println(value);
      newPrintedValues.add(value);
    }
    System.out.println();
    return newPrintedValues;
  }

  @Override
  public List<String> getPrintedValues() {
    return new ArrayList<>(printedValues);
  }

  @Override
  public InterpreterVisitor getPreviousVisitor() {
    return null;
  }
}
