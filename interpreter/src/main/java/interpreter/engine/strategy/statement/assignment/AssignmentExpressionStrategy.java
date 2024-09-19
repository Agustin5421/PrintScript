package interpreter.engine.strategy.statement.assignment;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.ValueCollector;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariableIdentifierFactory;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.statement.StatementStrategy;

public class AssignmentExpressionStrategy implements StatementStrategy {
  @Override
  public InterpreterEngine apply(AstNode node, InterpreterEngine engine) {
    AssignmentExpression assign = (AssignmentExpression) node;

    Literal<?> value = evaluateExpression(assign, engine);

    VariablesRepository repository = setVariable(assign, engine, value);

    return engine.setVariablesRepository(repository);
  }

  private Literal<?> evaluateExpression(AssignmentExpression assign, InterpreterEngine visitor) {
    ExpressionNode valueToEvaluate = assign.right();

    ValueCollector valueCollector = visitor.getValueCollector();

    return valueCollector.evaluate(valueToEvaluate).getValue();
  }

  private VariablesRepository setVariable(
      AssignmentExpression assign, InterpreterEngine visitor, Literal<?> evaluatedValue) {
    Identifier id = assign.left();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(id);
    VariablesRepository repository = visitor.getVariablesRepository();

    return repository.setNewVariable(varId, evaluatedValue);
  }
}
