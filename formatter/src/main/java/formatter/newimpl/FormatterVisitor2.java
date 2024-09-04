package formatter.newimpl;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import formatter.newimpl.strategy.FormattingStrategy;
import formatter.newimpl.strategy.OperatorConcatenationStrategy;
import formatter.newimpl.strategy.OperatorStrategy;
import formatter.newimpl.strategy.WhiteSpace;
import java.util.List;
import java.util.Map;

public class FormatterVisitor2 implements NodeVisitor {
  private final String currentCode;
  private final Map<AstNodeType, FormattingStrategy> strategies;

  public FormatterVisitor2(Map<AstNodeType, FormattingStrategy> strategies, String formattedCode) {
    this.currentCode = formattedCode;
    this.strategies = strategies;
  }

  public FormatterVisitor2(Map<AstNodeType, FormattingStrategy> strategies) {
    this(strategies, "");
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    FormatterVisitor2 visitor =
        (FormatterVisitor2) visitIdentifier(callExpression.methodIdentifier());
    String formattedCode = visitor.getCurrentCode();
    FormattingStrategy strategy = strategies.get(callExpression.getType());
    if (strategy != null) {
      formattedCode += strategy.apply(callExpression, this);
    }
    return new FormatterVisitor2(strategies, formattedCode);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    String formattedCode = formatAssignment(assignmentExpression);
    return new FormatterVisitor2(strategies, formattedCode);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    String formattedCode = "let ";
    FormatterVisitor2 visitor =
        (FormatterVisitor2) visitIdentifier(variableDeclaration.identifier());
    formattedCode += visitor.getCurrentCode();
    formattedCode += formatAssignment(variableDeclaration);
    return new FormatterVisitor2(strategies, formattedCode);
  }

  // Common method for assignment and var declaration (they differ
  // on the var dec having a let, constant visitor will also call this method)
  private String formatAssignment(AstNode node) {
    String formattedCode = "";
    FormattingStrategy strategy = strategies.get(node.getType());
    if (strategy != null) {
      formattedCode += strategy.apply(node, this);
    }
    // Visit the right side of the expression
    formattedCode += ((FormatterVisitor2) node.accept(this)).getCurrentCode();
    return formattedCode;
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new FormatterVisitor2(strategies, numberLiteral.value().toString());
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new FormatterVisitor2(strategies, stringLiteral.value());
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return new FormatterVisitor2(strategies, identifier.name());
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    String formattedCode =
        ((FormatterVisitor2) binaryExpression.left().accept(this)).getCurrentCode();
    // Applying the spaces and operator between the expression
    WhiteSpace whiteSpace = new WhiteSpace();
    FormattingStrategy strategy =
        new OperatorConcatenationStrategy(
            List.of(whiteSpace, new OperatorStrategy(binaryExpression.operator()), whiteSpace));
    formattedCode += strategy.apply(binaryExpression, this);
    // Formatting the right side of the expression
    formattedCode += ((FormatterVisitor2) binaryExpression.right().accept(this)).getCurrentCode();
    return new FormatterVisitor2(strategies, formattedCode);
  }

  public String getCurrentCode() {
    return currentCode;
  }

  // Returning the type equivalent of printscript (only working for string and numbers for now)
  private String getType(Expression expression) throws IllegalArgumentException {
    if (containsStringLiteral(expression)) {
      return "string";
    }
    return "number";
  }

  private boolean containsStringLiteral(Expression expression) {
    if (expression instanceof StringLiteral) {
      return true;
    } else if (expression instanceof BinaryExpression binaryExpression) {
      return containsStringLiteral(binaryExpression.left())
          || containsStringLiteral(binaryExpression.right());
    }
    return false;
  }
}
