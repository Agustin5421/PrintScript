package interpreter.visitor.strategy.conditional;

import ast.expressions.ExpressionNode;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.IfStatement;
import ast.visitor.NodeVisitor;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.ValueCollector;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;

public class IfStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor apply(AstNode node, NodeVisitor visitor) {
    IfStatement ifStatement = (IfStatement) node;
    ExpressionNode condition = ifStatement.getCondition();

    ValueCollector valueCollector = ((InterpreterVisitorV3) visitor).getValueCollector();
    ValueCollector conditionVisitor = (ValueCollector) condition.accept(valueCollector);
    Literal<?> conditionValue = conditionVisitor.getValue();

    Boolean conditionResult = (Boolean) conditionValue.value();

    NodeVisitor nestedVisitor = visitor;
    if (conditionResult) {
      for (AstNode statement : ifStatement.getThenBlockStatement()) {
        nestedVisitor = statement.accept(nestedVisitor);
      }
    } else {
      for (AstNode statement : ifStatement.getElseBlockStatement()) {
        nestedVisitor = statement.accept(nestedVisitor);
      }
    }

    return updateVisitor(visitor, nestedVisitor);
  }

  private NodeVisitor updateVisitor(NodeVisitor visitor, NodeVisitor nestedVisitor) {
    InterpreterVisitorV3 interpreterVisitor = (InterpreterVisitorV3) visitor;
    InterpreterVisitorV3 nestedInterpreterVisitor = (InterpreterVisitorV3) nestedVisitor;

    VariablesRepository oldVarRepo = interpreterVisitor.getVariablesRepository();
    VariablesRepository nestedVarRepo = nestedInterpreterVisitor.getVariablesRepository();
    VariablesRepository newVarRepo = oldVarRepo.update(nestedVarRepo);

    return new InterpreterVisitorV3(
        newVarRepo,
        interpreterVisitor.getStrategies(),
        interpreterVisitor.getOutputResult(),
        nestedInterpreterVisitor.getValueCollector());
  }
}
