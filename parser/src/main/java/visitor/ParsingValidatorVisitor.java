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
import exceptions.InvalidConstReassignmentException;
import exceptions.MismatchTypeException;
import exceptions.VariableAlreadyDeclaredException;
import exceptions.VariableNotDeclaredException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParsingValidatorVisitor implements NodeVisitor {
  private final Map<String, String> variablesRep =
      new HashMap<>(); // Map of variable names to their type
  private final Map<String, String> varKind =
      new HashMap<>(); // Map of variable names to their kind
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

    comparableType = getType(left, right);

    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    for (AstNode arg : callExpression.arguments()) {
      arg.accept(this);
    }

    comparableType = null;

    return this;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    if (!variablesRep.containsKey(identifier.name())) {
      throw new VariableNotDeclaredException(identifier.name());
    }
    comparableType = variablesRep.get(identifier.name());

    return this;
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    String name = assignmentExpression.left().name();
    if (!variablesRep.containsKey(name)) {
      throw new VariableNotDeclaredException(name);
    }
    if (Objects.equals(varKind.get(name), "const")) {
      throw new InvalidConstReassignmentException(assignmentExpression.left());
    }

    assignmentExpression.right().accept(this);

    String expected = variablesRep.get(name);
    if (!Objects.equals(expected, comparableType) && comparableType != null) {
      throw new MismatchTypeException(name, expected, comparableType);
    }

    comparableType = null;
    return this;
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration node) {
    String name = node.identifier().name();

    if (variablesRep.containsKey(name)) {
      throw new VariableAlreadyDeclaredException(node.identifier());
    }

    variablesRep.put(name, node.varType());
    varKind.put(name, node.kind());

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
      if (!variablesRep.containsKey(id.name())) {
        throw new VariableNotDeclaredException(id.name());
      }
      if (!Objects.equals(variablesRep.get(id.name()), "boolean")) {
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

  private static String getType(String left, String right) {
    if (Objects.equals(left, "boolean") || Objects.equals(right, "boolean")) {
      // TODO: fix message
      throw new UnsupportedOperationException(left);
    }
    return Objects.equals(left, "number") && Objects.equals(right, "number") ? "number" : "string";
  }
}
