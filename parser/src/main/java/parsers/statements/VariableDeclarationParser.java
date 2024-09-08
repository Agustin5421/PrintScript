package parsers.statements;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.statements.StatementNode;
import ast.statements.VariableDeclaration;
import exceptions.SyntaxException;
import java.util.List;
import parsers.Parser;
import token.Position;
import token.Token;

public class VariableDeclarationParser implements StatementParser {
  private final List<String> kinds;

  public VariableDeclarationParser(List<String> kinds) {
    this.kinds = kinds;
  }

  @Override
  public StatementNode parse(Parser parser, List<Token> tokens) {
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();

    Identifier identifier = new Identifier(tokens.get(1).value(), start, end);

    // TODO: improve exception messages
    if (!tokens.get(2).value().equals(":")) {
      throw new SyntaxException(
          "expected ':' at "
              + tokens.get(2).initialPosition().toString()
              + " but found '"
              + tokens.get(2).value()
              + "' instead.");
    }

    ExpressionNode value = parser.parseExpression(tokens.subList(5, tokens.size()));
    String kind = tokens.get(0).value();
    String type = tokens.get(3).value();

    return new VariableDeclaration(kind, identifier, value, type, start, end);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return kinds.contains(tokens.get(0).value());
  }
}
