package interpreter.visitor.strategy.vardec;

import ast.expressions.ExpressionNode;
import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;

public class VariableDeclarationStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    InterpreterVisitorV3 interpreterVisitor = (InterpreterVisitorV3) visitor;

    Literal<?> evaluatedValue = evaluateExpression(varDecNode, interpreterVisitor);

    VariablesRepository newRepository = addVariable(varDecNode, interpreterVisitor, evaluatedValue);

    return interpreterVisitor.setVariablesRepository(newRepository);
  }

  private Literal<?> evaluateExpression(
      VariableDeclaration varDecNode, InterpreterVisitorV3 visitor) {
    ExpressionNode valueToEvaluate = varDecNode.expression();

    ValueCollector valueCollector = visitor.getValueCollector();
    ValueCollector temp = (ValueCollector) valueCollector.visit(valueToEvaluate);

    return temp.getValue();
  }

  private VariablesRepository addVariable(
      VariableDeclaration varDecNode, InterpreterVisitorV3 visitor, Literal<?> evaluatedValue) {
    VariablesRepository repository = visitor.getVariablesRepository();
    VariableIdentifier varId = VariableIdentifierFactory.createVarIdFromVarDec(varDecNode);

    return repository.addNewVariable(varId, evaluatedValue);
  }
}
