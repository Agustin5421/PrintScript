package interpreter.engine.strategy.expression.callexpression;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.engine.ValueCollector;
import interpreter.engine.staticprovider.EnvLoader;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import interpreter.engine.strategy.expression.literal.LiteralHandler;
import java.util.List;

public class ReadEnvStrategy implements ExpressionStrategy {
  private final LiteralHandler literalHandler;

  public ReadEnvStrategy(LiteralHandler literalHandler) {
    this.literalHandler = literalHandler;
  }

  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    CallExpression callExp = (CallExpression) node;
    List<AstNode> arguments = callExp.arguments();

    if (arguments.size() != 1 || !(arguments.get(0) instanceof StringLiteral)) {
      throw new IllegalArgumentException("readEnv expects a single string argument");
    }

    StringLiteral envVar = ((StringLiteral) callExp.arguments().get(0));

    String input = EnvLoader.getValue(envVar.value());
    Literal<?> literalInput = literalHandler.getLiteral(callExp, input);

    return engine.setValue(literalInput);
  }
}
