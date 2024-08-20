package interpreter;

import static ast.utils.StatementValidator.isCallExpression;
import static ast.utils.StatementValidator.isVariableDeclaration;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import interpreter.runtime.ExpressionEvaluator;
import java.util.List;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import observers.Progressable;

public class Interpreter implements Progressable, NodeVisitor {
  private final List<Observer> observers;
  private int totalStatements;
  private int completedStatements;

  public Interpreter(List<Observer> observers) {
    this.observers = observers;
  }

  // This constructor is created in order to make the tests pass
  public Interpreter() {
    this.observers = List.of(new ProgressObserver(new ProgressPrinter()));
  }

//  este es el anterior sin el visitor, lo dejo por si acaso
//  public VariablesRepository executeProgram(Program program) {
//    VariablesRepository variablesRepository = new VariablesRepository();
//    totalStatements = program.statements().size();
//    completedStatements = 0;
//
//    for (AstNode statement : program.statements()) {
//      variablesRepository = evaluateStatement(statement, variablesRepository);
//      completedStatements++;
//      updateProgress();
//    }
//
//    return variablesRepository;
//  }

  public VariablesRepository executeProgram(Program program) {
    VariablesRepository variablesRepository = new VariablesRepository();
    totalStatements = program.statements().size();
    completedStatements = 0;

    for (AstNode statement : program.statements()) {
      statement.accept(this); // This is the only change
      completedStatements++;
      updateProgress();
    }

    return variablesRepository;
  }

  private void updateProgress() {
    assert observers != null;
    if (!observers.isEmpty()) {
      notifyObservers();
    }
  }


  // The following methods are for the Observer interface
  @Override
  public int getProgress() {
    return (int) (((double) completedStatements / totalStatements) * 100);
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }


  // The following methods are for the NodeVisitor interface
  @Override
  public void visit(CallExpression callExpression) {
    // Logic for interpreting CallExpression
    Identifier identifier = callExpression.methodIdentifier();
    List<AstNode> arguments = callExpression.arguments();
    boolean optionalParameters = callExpression.optionalParameters(); // TODO: how to use this?

    String name = "println";

    printlnMethod(new VariablesRepository(), identifier, name, arguments);
  }

  @Override
  public void visit(AssignmentExpression assignmentExpression) {
    // Logic for interpreting AssignmentExpression
    Identifier left = assignmentExpression.left();
    AstNode right = assignmentExpression.right();
    String operator = assignmentExpression.operator();

    // Evaluate the right-hand side expression
    ExpressionEvaluator evaluator = new ExpressionEvaluator(new VariablesRepository(), left.start().row());
    Literal<?> evaluatedRight = (Literal<?>) evaluator.evaluate(right);

    // Assign the evaluated value to the left identifier
    new VariablesRepository().addVariable(left.name(), evaluatedRight.value());
  }

  @Override
  public void visit(VariableDeclaration variableDeclaration) {
    // Logic for interpreting VariableDeclaration
    setVariable(variableDeclaration, new VariablesRepository());
  }

  @Override
  public void visit(NumberLiteral numberLiteral) {
    System.out.println("NumberLiteral: " + numberLiteral.value());
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    System.out.println("StringLiteral: " + stringLiteral.value());
  }

  @Override
  public void visit(Identifier identifier) {
    System.out.println("Identifier: " + identifier.name());
  }

  @Override
  public void visit(BinaryExpression binaryExpression) {
    // Logic for interpreting BinaryExpression
    ExpressionEvaluator evaluator = new ExpressionEvaluator(new VariablesRepository(), binaryExpression.start().row());
    AstNode result = evaluator.evaluate(binaryExpression);
    System.out.println("BinaryExpression result: " + ((Literal<?>) result).value());
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
