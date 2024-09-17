package visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import exceptions.InvalidConstReassignmentException;
import exceptions.MismatchTypeException;
import exceptions.VariableAlreadyDeclaredException;
import exceptions.VariableNotDeclaredException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParsingValidatorVisitor implements NodeVisitor {
  private final Map<String, String> variables =
      new HashMap<>(); // Map of variable names to their type
  private final Map<String, String> kinds = new HashMap<>(); // Map of variable names to their kind
  private String comparableType = null;

  public ParsingValidatorVisitor() {}

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    comparableType = "number";
    return this;
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    comparableType = "string";
    return this;
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    comparableType = "boolean";
    return this;
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    binaryExpression.left().accept(this);
    String left = comparableType;

    binaryExpression.right().accept(this);
    String right = comparableType;

    comparableType = combine(left, right);

    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    for (AstNode exp : callExpression.arguments()) {
      exp.accept(this);
    }
    comparableType = null;

    return this;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    if (!variables.containsKey(identifier.name())) {
      throw new VariableNotDeclaredException(identifier.name());
    }
    comparableType = variables.get(identifier.name());

    return this;
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    String name = assignmentExpression.left().name();
    if (!variables.containsKey(name)) {
      throw new VariableNotDeclaredException(name);
    }
    if (Objects.equals(kinds.get(name), "const")) {
      throw new InvalidConstReassignmentException(assignmentExpression.left());
    }

    assignmentExpression.right().accept(this);

    String expected = variables.get(name);
    if (!Objects.equals(expected, comparableType)) {
      throw new MismatchTypeException(name, expected, comparableType);
    }

    comparableType = null;
    return this;
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration node) {
    String name = node.identifier().name();

    if (variables.containsKey(name)) {
      throw new VariableAlreadyDeclaredException(node.identifier());
    }

    variables.put(name, node.varType());
    kinds.put(name, node.kind());

    if (node.expression() == null) {
      return this;
    }

    node.expression().accept(this);
    if (comparableType != null && !Objects.equals(node.varType(), comparableType)) {
      throw new MismatchTypeException(name, node.varType(), comparableType);
    }

    comparableType = null;
    return this;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    if (ifStatement.getCondition() instanceof Identifier id) {
      if (!variables.containsKey(id.name())) {
        throw new VariableNotDeclaredException(id.name());
      }
      if (!Objects.equals(variables.get(id.name()), "boolean")) {
        throw new MismatchTypeException(
            ifStatement.getCondition().toString(), "boolean", comparableType);
      }
    } else if (ifStatement.getCondition() instanceof BooleanLiteral) {
    } else {
      throw new MismatchTypeException(
          ifStatement.getCondition().toString(), "boolean", comparableType);
    }

    for (AstNode statement : ifStatement.getThenBlockStatement()) {
      statement.accept(this);
    }

    for (AstNode statement : ifStatement.getElseBlockStatement()) {
      statement.accept(this);
    }

    return this;
  }

  private static String combine(String left, String right) {
    if (Objects.equals(left, "boolean") || Objects.equals(right, "boolean")) {
      // TODO: fix message
      throw new UnsupportedOperationException(left);
    }
    return Objects.equals(left, "number") && Objects.equals(right, "number") ? "number" : "string";
  }
}
