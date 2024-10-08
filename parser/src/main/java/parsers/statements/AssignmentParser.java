package parsers.statements;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.statements.AssignmentExpression;
import ast.statements.StatementNode;
import exceptions.UnexpectedTokenException;
import java.util.List;
import parsers.Parser;
import position.Position;
import token.Token;
import token.types.TokenSyntaxType;

public class AssignmentParser implements StatementParser {

  @Override
  public StatementNode parse(Parser parser, List<Token> tokens) {
    validateSyntax(tokens);

    Position leftStart = tokens.get(0).initialPosition();
    Position leftEnd = tokens.get(0).finalPosition();

    Position rightEnd = tokens.get(2).finalPosition();

    Identifier left = new Identifier(tokens.get(0).value(), leftStart, leftEnd);
    ExpressionNode right = parser.parseExpression(tokens.subList(2, tokens.size()));

    return new AssignmentExpression(left, right, tokens.get(1).value(), leftStart, rightEnd);
  }

  private void validateSyntax(List<Token> tokens) {
    Token token = tokens.get(1);
    if (!token.value().equals("=")) {
      throw new UnexpectedTokenException(token, "=");
    }
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.get(0).tokenType() == TokenSyntaxType.IDENTIFIER && tokens.size() >= 2;
  }
}
