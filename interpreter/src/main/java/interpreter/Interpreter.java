package interpreter;

import static ast.utils.StatementValidator.*;

import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.ASTNode;
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

    for (ASTNode statement : program.statements()) {
      variablesRepository = evaluateStatement(statement, variablesRepository);
      completedStatements++;
      updateProgress();
    }

    return variablesRepository;
  }

  private void updateProgress() {
    int progress = (int) (((double) completedStatements / totalStatements) * 100);
    if (observer != null) observer.update("interpreter", progress);
  }

  @Override
  public int getProgress() {
    return (int) (((double) completedStatements / totalStatements) * 100);
  }

  private VariablesRepository evaluateStatement(
      ASTNode statement, VariablesRepository variablesRepository) {
    if (isVariableDeclaration(statement)) {
      return setVariable((VariableDeclaration) statement, variablesRepository);
    } else if (isCallExpression(statement)) {
      CallExpression callExpression = (CallExpression) statement;
      Identifier identifier = callExpression.methodIdentifier();
      List<ASTNode> arguments = callExpression.arguments();
      boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

      String name =
          "println"; // TODO: como hacerlo generico? tal vez un enum con todos los tipos pero no se
      // si es buena idea
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
      List<ASTNode> arguments) {
    if (identifier.name().equals(name)) {
      ExpressionEvaluator expressionEvaluator =
          new ExpressionEvaluator(variablesRepository, identifier.start().row());
      for (ASTNode argument : arguments) {
        System.out.println(((Literal<?>) expressionEvaluator.evaluate(argument)).value());
      }
      System.out.println();
    }
  }
}
