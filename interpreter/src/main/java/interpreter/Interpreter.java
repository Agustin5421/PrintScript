package interpreter;

import static ast.utils.StatementValidator.isCallExpression;
import static ast.utils.StatementValidator.isVariableDeclaration;

import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import interpreter.runtime.ExpressionEvaluator;
import java.util.List;
import observers.ProgressObserver;
import observers.Progressable;

public class Interpreter implements Progressable {
  private final ProgressObserver observer;
  private int totalStatements;
  private int completedStatements;

  public Interpreter(ProgressObserver observer) {
    this.observer = observer;
  }

  public Interpreter() {
    this.observer = null;
  }

  public VariablesRepository executeProgram(Program program) {
    VariablesRepository variablesRepository = new VariablesRepository();
    totalStatements = program.statements().size();
    completedStatements = 0;

    for (AstNode statement : program.statements()) {
      variablesRepository = evaluateStatement(statement, variablesRepository);
      completedStatements++;
      updateProgress();
    }

    return variablesRepository;
  }

  private void updateProgress() {
    int progress = (int) (((double) completedStatements / totalStatements) * 100);
    if (observer != null) {
      observer.update("interpreter", progress);
    }
  }

  @Override
  public int getProgress() {
    return (int) (((double) completedStatements / totalStatements) * 100);
  }

  private VariablesRepository evaluateStatement(
      AstNode statement, VariablesRepository variablesRepository) {
    if (isVariableDeclaration(statement)) {
      return setVariable((VariableDeclaration) statement, variablesRepository);
    } else if (isCallExpression(statement)) {
      CallExpression callExpression = (CallExpression) statement;
      Identifier identifier = callExpression.methodIdentifier();
      List<AstNode> arguments = callExpression.arguments();
      boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

      String name = "println";

      printlnMethod(variablesRepository, identifier, "println", arguments);

      return variablesRepository;
    }

    return variablesRepository;
  }

  private static VariablesRepository setVariable(
      VariableDeclaration statement, VariablesRepository variablesRepository) {
    String name = statement.identifier().name();
    ExpressionEvaluator expressionEvaluator =
        new ExpressionEvaluator(variablesRepository, statement.start().row());
    Literal<?> literal = (Literal<?>) expressionEvaluator.evaluate(statement.expression());
    Object value = literal.value();

    return variablesRepository.addVariable(name, value);
  }

  private void printlnMethod(
      VariablesRepository variablesRepository,
      Identifier identifier,
      String name,
      List<AstNode> arguments) {
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
