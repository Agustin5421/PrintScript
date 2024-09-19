package interpreter.engine.strategy.expression.identifier;

import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.AstNode;
import interpreter.engine.ValueCollector;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariableIdentifierFactory;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.expression.ExpressionStrategy;

public class IdentifierStrategy implements ExpressionStrategy {
  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    Identifier identifierNode = (Identifier) node;
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(identifierNode);

    VariablesRepository repository = engine.getVariablesRepository();
    Literal<?> value = repository.getNewVariable(varId);

    return engine.setValue(value);
  }
}
