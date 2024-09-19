package interpreter.engine.strategy.statement.conditional;

import ast.expressions.ExpressionNode;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.IfStatement;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.ValueCollector;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.statement.StatementStrategy;

public class IfStrategy implements StatementStrategy {
  @Override
  public InterpreterEngine apply(AstNode node, InterpreterEngine engine) {
    IfStatement ifStatement = (IfStatement) node;
    ExpressionNode condition = ifStatement.getCondition();

    ValueCollector valueCollector = engine.getValueCollector();
    ValueCollector conditionVisitor = valueCollector.evaluate(condition);
    Literal<?> conditionValue = conditionVisitor.getValue();

    Boolean conditionResult = (Boolean) conditionValue.value();

    InterpreterEngine nestedEngine = engine;
    if (conditionResult) {
      for (AstNode statement : ifStatement.getThenBlockStatement()) {
        nestedEngine = nestedEngine.interpret(statement);
      }
    } else {
      for (AstNode statement : ifStatement.getElseBlockStatement()) {
        nestedEngine = nestedEngine.interpret(statement);
      }
    }

    return updateEngine(engine, nestedEngine);
  }

  private InterpreterEngine updateEngine(InterpreterEngine engine, InterpreterEngine nestedEngine) {
    VariablesRepository oldVarRepo = engine.getVariablesRepository();
    VariablesRepository nestedVarRepo = nestedEngine.getVariablesRepository();
    VariablesRepository newVarRepo = oldVarRepo.update(nestedVarRepo);

    return engine.setVariablesRepository(newVarRepo);
  }
}
