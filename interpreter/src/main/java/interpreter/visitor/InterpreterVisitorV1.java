package interpreter.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import interpreter.VariablesRepository;
import interpreter.runtime.ExpressionEvaluator;
import java.util.List;

public class InterpreterVisitorV1 implements NodeVisitor { // }, NodeVisitor2 {
  private final VariablesRepository variablesRepository;

  public InterpreterVisitorV1(VariablesRepository variablesRepository) {
    this.variablesRepository = variablesRepository;
  }

  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
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

  @Override
  public NodeVisitor visit(AstNode node) {
    AstNodeType nodeType = node.getNodeType();
    if (nodeType == AstNodeType.VARIABLE_DECLARATION) {
      return visitVarDec((VariableDeclaration) node);
    } else if (nodeType == AstNodeType.ASSIGNMENT_EXPRESSION) {
      return visitAssignmentExpression((AssignmentExpression) node);
    } else if (nodeType == AstNodeType.CALL_EXPRESSION) {
      return visitCallExpression((CallExpression) node);
    } else if (nodeType == AstNodeType.NUMBER_LITERAL) {
      return visitNumberLiteral((NumberLiteral) node);
    } else if (nodeType == AstNodeType.STRING_LITERAL) {
      return visitStringLiteral((StringLiteral) node);
    } else if (nodeType == AstNodeType.IDENTIFIER) {
      return visitIdentifier((Identifier) node);
    } else if (nodeType == AstNodeType.BINARY_EXPRESSION) {
      return visitBinaryExpression((BinaryExpression) node);
    } else {
      throw new IllegalArgumentException("Node not supported in this version :( ");
    }
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

  private void printlnMethod(Identifier identifier, String name, List<AstNode> arguments) {
    if (identifier.name().equals(name)) {
      ExpressionEvaluator expressionEvaluator =
          new ExpressionEvaluator(variablesRepository, identifier.start().row());
      for (AstNode argument : arguments) {
        System.out.println(((Literal<?>) expressionEvaluator.evaluate(argument)).value());
      }
      System.out.println();
    }
  }
}
