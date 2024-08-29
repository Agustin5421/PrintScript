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
        variablesRepository.addVariable(left.name(), evaluatedRight);
    return new AstNodeVisitor(newVariablesRepository);
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
    //    ExpressionEvaluator evaluator =
    //        new ExpressionEvaluator(variablesRepository, binaryExpression.start().row());
    //    AstNode result = evaluator.evaluate(binaryExpression);
    //    System.out.println("BinaryExpression result: " + ((Literal<?>) result).value());
    return this;
  }

  private NodeVisitor setVariable(VariableDeclaration statement) {
    String name = statement.identifier().name();

    if (variablesRepository.getVariables().containsKey(name)) {
      throw new IllegalArgumentException("Variable " + name + " is already defined");
    } // cambiar a una excepcion mas concreta de variable ya definida

    ExpressionEvaluator expressionEvaluator =
        new ExpressionEvaluator(variablesRepository, statement.start().row());
    Literal<?> value = (Literal<?>) expressionEvaluator.evaluate(statement.expression());
    // Object value = literal.value();

    VariablesRepository newVariablesRepository = variablesRepository.addVariable(name, value);
    return new AstNodeVisitor(newVariablesRepository);
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
