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
  private final Map<String, String> variablesRep =
      new HashMap<>(); // Map of variable names to their type
  private final Map<String, String> varKind =
      new HashMap<>(); // Map of variable names to their kind
  private String comparableType = null;

  public ParsingValidatorVisitor() {}

  @Override
  public NodeVisitor visit(AstNode node) {
    if (node instanceof VariableDeclaration) {
      return visitVarDec((VariableDeclaration) node);
    } else if (node instanceof IfStatement) {
      return visitIfStatement((IfStatement) node);
    } else if (node instanceof BooleanLiteral) {
      return visitBooleanLiteral((BooleanLiteral) node);
    } else if (node instanceof CallExpression) {
      return visitCallExpression((CallExpression) node);
    } else if (node instanceof AssignmentExpression) {
      return visitAssignmentExpression((AssignmentExpression) node);
    } else if (node instanceof BinaryExpression) {
      return visitBinaryExpression((BinaryExpression) node);
    } else if (node instanceof NumberLiteral) {
      return visitNumberLiteral((NumberLiteral) node);
    } else if (node instanceof StringLiteral) {
      return visitStringLiteral((StringLiteral) node);
    } else if (node instanceof Identifier) {
      return visitIdentifier((Identifier) node);
    }
    return this;
  }

  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    comparableType = "number";
    return this;
  }

  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    comparableType = "string";
    return this;
  }

  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    comparableType = "boolean";
    return this;
  }

  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    binaryExpression.left().accept(this);
    String left = comparableType;

    binaryExpression.right().accept(this);
    String right = comparableType;

    comparableType = getType(left, right);

    return this;
  }

  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    for (AstNode arg : callExpression.arguments()) {
      arg.accept(this);
    }

    comparableType = null;

    return this;
  }

  public NodeVisitor visitIdentifier(Identifier identifier) {
    if (!variablesRep.containsKey(identifier.name())) {
      throw new VariableNotDeclaredException(identifier.name());
    }
    comparableType = variablesRep.get(identifier.name());

    return this;
  }

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
