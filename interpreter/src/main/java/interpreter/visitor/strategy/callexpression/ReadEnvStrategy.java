package interpreter.visitor.strategy.callexpression;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.staticprovider.EnvLoader;
import interpreter.visitor.strategy.InterpretingStrategy;
import java.util.List;

public class ReadEnvStrategy implements InterpretingStrategy {
  private final LiteralHandler literalHandler;

  public ReadEnvStrategy(LiteralHandler literalHandler) {
    this.literalHandler = literalHandler;
  }

  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    CallExpression callExp = (CallExpression) node;
    List<AstNode> arguments = callExp.arguments();

    if (arguments.size() != 1 || !(arguments.get(0) instanceof StringLiteral)) {
      throw new IllegalArgumentException("readEnv expects a single string argument");
    }

    StringLiteral envVar = ((StringLiteral) callExp.arguments().get(0));

    String input = EnvLoader.getValue(envVar.value());
    Literal<?> literalInput = literalHandler.getLiteral(callExp, input);

    ValueCollector valueCollector = (ValueCollector) visitor;
    return valueCollector.setValue(literalInput);
  }
}
