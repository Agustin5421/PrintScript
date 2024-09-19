package parsers.statements;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.statements.StatementNode;
import ast.statements.VariableDeclaration;
import exceptions.UnexpectedTokenException;
import exceptions.UnsupportedDataType;
import java.util.List;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;

public class VariableDeclarationParser implements StatementParser {
  private final List<String> kinds;
  private final List<String> types;

  public VariableDeclarationParser(List<String> kinds, List<String> types) {
    this.kinds = kinds;
    this.types = types;
  }

  @Override
  public StatementNode parse(Parser parser, List<Token> tokens) {
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();

    String name = tokens.get(1).value();

    Identifier identifier = new Identifier(name, start, end);

    if (!tokens.get(2).value().equals(":")) {
      throw new UnexpectedTokenException(tokens.get(2), ":");
    }

    ExpressionNode value;

    if (tokens.get(4).nodeType() != TokenSyntaxType.ASSIGNATION) {
      value = null;
    } else {
      value = parser.parseExpression(tokens.subList(5, tokens.size()));
    }

    String kind = tokens.get(0).value();

    String type = getType(tokens.get(3));

    return new VariableDeclaration(kind, identifier, value, type, start, end);
  }

  private String getType(Token token) {
    if (types.contains(token.value())) {
      return token.value();
    }
    throw new UnsupportedDataType(token);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return kinds.contains(tokens.get(0).value());
  }
}
