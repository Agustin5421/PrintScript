package parsers.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.utils.ExpressionParserProvider;
import java.util.List;
import token.Position;
import token.Token;
import token.types.TokenTagType;

public class AssignmentParser implements StatementParser {

  @Override
  public AstNode parse(List<Token> tokens) {
    validateSyntax(tokens);

    Position leftStart = tokens.get(0).getInitialPosition();
    Position leftEnd = tokens.get(0).getFinalPosition();

    Position rightEnd = tokens.get(2).getFinalPosition();

    Identifier left = new Identifier(tokens.get(0).getValue(), leftStart, leftEnd);
    Expression right = ExpressionParserProvider.parse(tokens.subList(2, tokens.size()));

    return new AssignmentExpression(left, right, tokens.get(1).getValue(), leftStart, rightEnd);
  }

  private void validateSyntax(List<Token> tokens) {
    if (!shouldParse(tokens)) {
      throw new IllegalArgumentException("Invalid tokens for AssignmentExpression");
    }

    if (!tokens.get(1).getValue().equals("=")) {
      throw new IllegalArgumentException("Invalid tokens for AssignmentExpression");
    }
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.get(0).getType() == TokenTagType.IDENTIFIER && tokens.size() >= 2;
  }
}
