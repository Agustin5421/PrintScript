package interpreter;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import interpreter.runtime.ExpressionEvaluator;

import java.util.List;

public class AstNodeVisitor implements NodeVisitor {
  private final VariablesRepository variablesRepository;

  public AstNodeVisitor(VariablesRepository variablesRepository) {
    this.variablesRepository = variablesRepository;
  }

  @Override
  public NodeVisitor visit(CallExpression callExpression) {
    List<AstNode> arguments = callExpression.arguments();
    Identifier identifier = callExpression.methodIdentifier();
    boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

    String name = "println";

    printlnMethod(identifier, name, arguments);
    return this;
  }

  @Override
  public NodeVisitor visit(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    AstNode right = assignmentExpression.right();
    String operator = assignmentExpression.operator();

    ExpressionEvaluator evaluator = new ExpressionEvaluator(variablesRepository, left.start().row());
    Literal<?> evaluatedRight = (Literal<?>) evaluator.evaluate(right);

    variablesRepository.addVariable(left.name(), evaluatedRight.value());
    return this;
  }

  @Override
  public NodeVisitor visit(VariableDeclaration variableDeclaration) {
    setVariable(variableDeclaration);
    return this;
  }

  @Override
  public NodeVisitor visit(NumberLiteral numberLiteral) {
    System.out.println("NumberLiteral: " + numberLiteral.value());
    variablesRepository.addVariable("it", numberLiteral.value());
  }

  @Override
  public NodeVisitor visit(StringLiteral stringLiteral) {
    System.out.println("StringLiteral: " + stringLiteral.value());
  }

  @Override
  public NodeVisitor visit(Identifier identifier) {
    System.out.println("Identifier: " + identifier.name());
  }

  @Override
  public NodeVisitor visit(BinaryExpression binaryExpression) {
    ExpressionEvaluator evaluator = new ExpressionEvaluator(variablesRepository, binaryExpression.start().row());
    AstNode result = evaluator.evaluate(binaryExpression);
    System.out.println("BinaryExpression result: " + ((Literal<?>) result).value());
  }

  private void setVariable(VariableDeclaration statement) {
    String name = statement.identifier().name();
    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(variablesRepository, statement.start().row());
    Literal<?> literal = (Literal<?>) expressionEvaluator.evaluate(statement.expression());
    Object value = literal.value();

    variablesRepository.addVariable(name, value);
  }

  private void printlnMethod(Identifier identifier, String name, List<AstNode> arguments) {
    if (identifier.name().equals(name)) {
      ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(variablesRepository, identifier.start().row());
      for (AstNode argument : arguments) {
        //variablesRepository.addVariable("it", expressionEvaluator.evaluate(argument)).value());
        System.out.println(((Literal<?>) expressionEvaluator.evaluate(argument)).value());
      }
      System.out.println();
    }
  }
}