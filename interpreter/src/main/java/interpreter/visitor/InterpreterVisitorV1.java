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
import interpreter.evaluator.BinaryExpressionEvaluator;
import java.util.List;
import token.Position;

public class InterpreterVisitorV1 implements NodeVisitor { // }, NodeVisitor2 {
  private final VariablesRepository variablesRepository;
  private final Literal<?> value;
  private final BinaryExpressionEvaluator binaryExpressionEvaluator =
      new BinaryExpressionEvaluator();

  public InterpreterVisitorV1(VariablesRepository variablesRepository, Literal<?> value) {
    this.variablesRepository = variablesRepository;
    this.value = value;
  }

  public InterpreterVisitorV1(VariablesRepository variablesRepository) {
    this(variablesRepository, new NumberLiteral(0, new Position(0, 0), new Position(0, 0)));
  }

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
    boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

    String name = "println";

    printlnMethod(identifier, name, arguments);
    return this;
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();

    Literal<?> evaluatedRight =
        ((InterpreterVisitorV1) assignmentExpression.right().accept(this)).getValue();

    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(left, evaluatedRight);
    return new InterpreterVisitorV1(newVariablesRepository, value);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return setVariable(variableDeclaration);
  }

  // que tire excepcion  estos q no deberian de llegar
  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new InterpreterVisitorV1(variablesRepository, numberLiteral);
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new InterpreterVisitorV1(variablesRepository, stringLiteral);
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return this;
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    Literal<?> value = binaryExpressionEvaluator.evaluate(binaryExpression, this);
    return new InterpreterVisitorV1(variablesRepository, value);
  }

  private NodeVisitor setVariable(VariableDeclaration statement) {
    Identifier name = statement.identifier();

    if (variablesRepository.getVariables().containsKey(name)) {
      throw new IllegalArgumentException("Variable " + name + " is already defined");
    } // cambiar a una excepcion mas concreta de variable ya definida

    Literal<?> value = ((InterpreterVisitorV1) statement.expression().accept(this)).getValue();

    VariablesRepository newVariablesRepository = variablesRepository.addVariable(name, value);
    return new InterpreterVisitorV1(newVariablesRepository, value);
  }

  private void printlnMethod(Identifier identifier, String name, List<AstNode> arguments) {
    if (identifier.name().equals(name)) {
      for (AstNode argument : arguments) {
        System.out.println(((InterpreterVisitorV1) argument.accept(this)).getValue());
      }
      System.out.println();
    }
  }
}
