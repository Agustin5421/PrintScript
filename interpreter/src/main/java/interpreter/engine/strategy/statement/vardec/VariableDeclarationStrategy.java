package interpreter.engine.strategy.statement.vardec;

import ast.expressions.ExpressionNode;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.ValueCollector;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariableIdentifierFactory;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.statement.StatementStrategy;

public class VariableDeclarationStrategy implements StatementStrategy {
  @Override
  public InterpreterEngine apply(AstNode node, InterpreterEngine engine) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;

    Literal<?> evaluatedValue = evaluateExpression(varDecNode, engine);

    VariablesRepository newRepository = addVariable(varDecNode, engine, evaluatedValue);

    return engine.setVariablesRepository(newRepository);
  }

  private Literal<?> evaluateExpression(VariableDeclaration varDecNode, InterpreterEngine engine) {
    ExpressionNode valueToEvaluate = varDecNode.expression();

    if (valueToEvaluate == null) {
      return null;
    }

    ValueCollector valueCollector = engine.getValueCollector();
    ValueCollector temp = valueCollector.evaluate(valueToEvaluate);

    return temp.getValue();
  }

  private VariablesRepository addVariable(
      VariableDeclaration varDecNode, InterpreterEngine engine, Literal<?> evaluatedValue) {
    VariablesRepository repository = engine.getVariablesRepository();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromVarDec(varDecNode);

    return repository.addNewVariable(varId, evaluatedValue);
  }
}
