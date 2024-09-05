package parsers.statements;

import ast.expressions.Expression;
import ast.statements.IfStatement;
import ast.statements.Statement;
import java.util.ArrayList;
import java.util.List;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;

public class IfParser implements StatementParser {
  @Override
  public Statement parse(Parser parser, List<Token> tokens) {
    Expression condition = parser.parseExpression(List.of(tokens.get(2)));

    List<Token> thenBodyTokens = getThenBodyTokens(tokens);
    List<Token> elseBodyTokens = getElseBodyTokens(tokens);

    List<Statement> thenBodyStatements = parser.parseBlock(thenBodyTokens);
    List<Statement> elseBodyStatements = parser.parseBlock(elseBodyTokens);

    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();
    return new IfStatement(start, end, condition, thenBodyStatements, elseBodyStatements);
  }

  private List<Token> getThenBodyTokens(List<Token> tokens) {
    List<Token> thenBodyTokens = new ArrayList<>();
    int braceCount = 0;

    for (int i = 4; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      if (token.nodeType() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      } else if (token.nodeType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        if (braceCount == 0) {
          break;
        }
      }
      thenBodyTokens.add(token);
    }

    return removeBraces(thenBodyTokens);
  }

  private List<Token> getElseBodyTokens(List<Token> tokens) {
    List<Token> elseBodyTokens = new ArrayList<>();
    int braceCount = 0;

    for (int i = 4; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      if (token.nodeType() == TokenSyntaxType.ELSE) {
        continue;
      }

      if (token.nodeType() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      } else if (token.nodeType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        if (braceCount == 0) {
          break;
        }
      }
      elseBodyTokens.add(token);
    }
    return removeBraces(elseBodyTokens);
  }

  private List<Token> removeBraces(List<Token> tokens) {
    return tokens.subList(1, tokens.size() - 1);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.get(0).nodeType() == TokenSyntaxType.IF;
  }
}
