package parsers.statements;

import ast.expressions.Expression;
import ast.statements.IfStatement;
import ast.statements.StatementNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;

public class IfParser implements StatementParser {
  @Override
  public StatementNode parse(Parser parser, List<Token> tokens) {
    Expression condition = parser.parseExpression(List.of(tokens.get(2)));

    List<Token> thenBody = tokens.subList(4, tokens.size());

    Map<String, List<Token>> bodyTokens = getBodyTokens(thenBody);

    List<StatementNode> thenBodyStatements = parser.parseBlock(bodyTokens.get("then"));
    List<StatementNode> elseBodyStatements = parser.parseBlock(bodyTokens.get("else"));

    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();
    return new IfStatement(start, end, condition, thenBodyStatements, elseBodyStatements);
  }

  private Map<String, List<Token>> getBodyTokens(List<Token> tokens) {
    List<Token> thenBodyTokens = new ArrayList<>();
    int braceCount = 0;
    boolean hasElse = false;

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);

      if (token.nodeType() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      }

      thenBodyTokens.add(token);

      if (token.nodeType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;

        if (braceCount == 0) {
          if (i + 1 < tokens.size() && tokens.get(i + 1).nodeType() == TokenSyntaxType.ELSE) {
            hasElse = true;
          }
          break;
        }
      }
    }

    List<Token> elseBodyTokens = new ArrayList<>();
    if (hasElse) {
      for (int i = thenBodyTokens.size() + 1; i < tokens.size(); i++) {
        elseBodyTokens.add(tokens.get(i));
      }
    }

    return Map.of("then", removeBraces(thenBodyTokens), "else", removeBraces(elseBodyTokens));
  }

  private List<Token> removeBraces(List<Token> tokens) {
    if (tokens.size() >= 2
        && tokens.get(0).nodeType() == TokenSyntaxType.OPEN_BRACES
        && tokens.get(tokens.size() - 1).nodeType() == TokenSyntaxType.CLOSE_BRACES) {
      return tokens.subList(1, tokens.size() - 1);
    }
    return tokens;
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    if (tokens.size() < 6) {
      return false;
    }

    return tokens.get(0).nodeType() == TokenSyntaxType.IF;
  }
}
