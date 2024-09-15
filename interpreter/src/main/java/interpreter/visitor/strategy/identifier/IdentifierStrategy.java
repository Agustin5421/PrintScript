package interpreter.visitor.strategy.identifier;

import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;

public class IdentifierStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    Identifier identifierNode = (Identifier) node;
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(identifierNode);
    ValueCollector valueCollector = (ValueCollector) visitor;

    VariablesRepository repository = valueCollector.getVariablesRepository();
    Literal<?> value = repository.getNewVariable(varId);

    return valueCollector.setValue(value);
  }
}
