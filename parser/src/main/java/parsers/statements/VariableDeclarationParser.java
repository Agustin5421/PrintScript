package parsers.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.statements.Statement;
import ast.statements.VariableDeclaration;
import exceptions.SyntaxException;
import exceptions.UnsupportedDataType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import parsers.Parser;
import token.Position;
import token.Token;

public class VariableDeclarationParser implements StatementParser {
  @Override
  public Statement parse(Parser parser, List<Token> tokens) {
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();
    Map<String, Boolean> modifiers = Map.of("isModifiable", true);

    Identifier identifier = new Identifier(tokens.get(1).value(), start, end, modifiers);

    // TODO: improve exception messages
    if (!tokens.get(2).value().equals(":")) {
      throw new SyntaxException(
          "expected ':' at "
              + tokens.get(2).initialPosition().toString()
              + " but found '"
              + tokens.get(2).value()
              + "' instead.");
    }

    if (!tokens.get(3).value().equals("number") && !tokens.get(3).value().equals("string")) {
      throw new UnsupportedDataType(
          "Expected 'number' or 'string' at "
              + tokens.get(3).initialPosition().toString()
              + " but found "
              + tokens.get(3).value()
              + " instead.");
    }

    Expression value = parser.parseExpression(tokens.subList(5, tokens.size()));

    return new VariableDeclaration(identifier, value, start, end);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return Objects.equals(tokens.get(0).value(), "let");
  }
}
