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
import interpreter.runtime.ExpressionEvaluator;
import java.util.List;

public class InterpreterVisitorV1 implements InterpreterVisitor { // }, NodeVisitor2 {
  private final VariablesRepository variablesRepository;

  public InterpreterVisitorV1(VariablesRepository variablesRepository) {
    this.variablesRepository = variablesRepository;
  }

  @Override
  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
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

    String name = identifier.name();

    if (name.equals("println")) {
      printlnMethod(identifier, arguments);
      return this;
    } else {
      throw new IllegalArgumentException(name + " not supported in this version :( ");
    }
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    AstNode right = assignmentExpression.right();
    String operator = assignmentExpression.operator();

    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(variablesRepository, left.start().row());
    Literal<?> evaluatedRight = (Literal<?>) evaluator.evaluate(right);

    VariablesRepository newVariablesRepository =
        variablesRepository.addVariable(left, evaluatedRight);
    return new InterpreterVisitorV1(newVariablesRepository);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return setVariable(variableDeclaration);
  }

  // que tire excepcion  estos q no deberian de llegar
  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return this;
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return this;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return this;
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return this;
  }

  private NodeVisitor setVariable(VariableDeclaration statement) {
    Identifier name = statement.identifier();

    if (variablesRepository.getVariables().containsKey(name)) {
      throw new IllegalArgumentException("Variable " + name + " is already defined");
    } // cambiar a una excepcion mas concreta de variable ya definida

    ExpressionEvaluator expressionEvaluator =
        new ExpressionEvaluator(variablesRepository, statement.start().row());
    Literal<?> value = (Literal<?>) expressionEvaluator.evaluate(statement.expression());
    // Object value = literal.value();

    VariablesRepository newVariablesRepository = variablesRepository.addVariable(name, value);
    return new InterpreterVisitorV1(newVariablesRepository);
  }

  private void printlnMethod(Identifier identifier, List<AstNode> arguments) {
    ExpressionEvaluator expressionEvaluator =
        new ExpressionEvaluator(variablesRepository, identifier.start().row());
    for (AstNode argument : arguments) {
      System.out.println(((Literal<?>) expressionEvaluator.evaluate(argument)).value());
    }
    System.out.println();
  }
}
