package interpreter.visitor.strategy.assignment;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;

public class AssignmentExpressionStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    AssignmentExpression assign = (AssignmentExpression) node;
    InterpreterVisitorV3 visitorV3 = (InterpreterVisitorV3) visitor;

    Literal<?> value = evaluateExpression(assign, visitorV3);

    VariablesRepository repository = setVariable(assign, visitorV3, value);

    return visitorV3.setVariablesRepository(repository);
  }

  private Literal<?> evaluateExpression(AssignmentExpression assign, InterpreterVisitorV3 visitor) {
    ExpressionNode valueToEvaluate = assign.right();

    ValueCollector valueCollector = visitor.getValueCollector();
    ValueCollector temp = (ValueCollector) valueCollector.visit(valueToEvaluate);

    return temp.getValue();
  }

  private VariablesRepository setVariable(
      AssignmentExpression assign, InterpreterVisitorV3 visitor, Literal<?> evaluatedValue) {
    Identifier id = assign.left();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromIdentifier(id);
    VariablesRepository repository = visitor.getVariablesRepository();

    return repository.setNewVariable(varId, evaluatedValue);
  }
}
