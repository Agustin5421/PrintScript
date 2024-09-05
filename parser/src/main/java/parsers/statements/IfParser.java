package parsers.statements;

import ast.expressions.Expression;
import ast.statements.IfStatement;
import ast.statements.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;

public class IfParser implements StatementParser {
  @Override
  public Statement parse(Parser parser, List<Token> tokens) {
    Expression condition = parser.parseExpression(List.of(tokens.get(2)));

    List<Token> thenBody = tokens.subList(4, tokens.size());

    Map<String, List<Token>> bodyTokens = getBodyTokens(thenBody);

    List<Statement> thenBodyStatements = parser.parseBlock(bodyTokens.get("then"));
    List<Statement> elseBodyStatements = parser.parseBlock(bodyTokens.get("else"));

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

      if (token.nodeType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        thenBodyTokens.add(token);

        if (braceCount == 0) {
          try {
            if (tokens.get(i + 1).nodeType() == TokenSyntaxType.ELSE) {
              hasElse = true;
            }
          } catch (IndexOutOfBoundsException e) {

          }
          break;
        }
      }

      thenBodyTokens.add(token);
    }

    List<Token> elseBodyTokens = new ArrayList<>();
    if (hasElse) {
      for (int i = thenBodyTokens.size() + 1; i < tokens.size(); i++) {
        Token token = tokens.get(i);
        elseBodyTokens.add(token);
      }
    }

    return Map.of("then", removeBraces(thenBodyTokens), "else", removeBraces(elseBodyTokens));
  }

  private List<Token> removeBraces(List<Token> tokens) {
    if (tokens.size() < 2) {
      return tokens;
    }
    return tokens.subList(1, tokens.size() - 1);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    if (tokens.size() < 6) {
      return false;
    }

    return tokens.get(0).nodeType() == TokenSyntaxType.IF;
  }
}
