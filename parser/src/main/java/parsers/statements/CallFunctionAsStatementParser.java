package parsers.statements;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.StatementNode;
import exceptions.SyntaxException;
import exceptions.UnexpectedTokenException;
import java.util.ArrayList;
import java.util.List;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;
import token.types.TokenType;

public class CallFunctionAsStatementParser implements StatementParser {
  private final List<String> reservedWords;

  public CallFunctionAsStatementParser(List<String> reservedWords) {
    this.reservedWords = reservedWords;
  }

  @Override
  public StatementNode parse(Parser parser, List<Token> tokens) {
    String functionName = tokens.get(0).value();
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();

    // Function name
    Identifier identifier = new Identifier(functionName, start, end);

    // Arguments
    List<Token> subList = tokens.subList(1, tokens.size());
    List<List<Token>> arguments = extractArguments(subList);

    List<AstNode> argumentExpressions = new ArrayList<>();
    for (List<Token> token : arguments) {
      AstNode argument = parser.parseExpression(token);
      argumentExpressions.add(argument);
    }

    return new CallExpression(identifier, argumentExpressions, start, end);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return reservedWords.contains(tokens.get(0).value());
  }

  public static List<List<Token>> extractArguments(List<Token> tokens) {
    List<List<Token>> argumentTokens = new ArrayList<>();
    List<Token> currentArgument = new ArrayList<>();
    boolean inArguments = false;
    int openParentheses = 0;

    for (Token token : tokens) {
      TokenType type = token.nodeType();

      if (type == TokenSyntaxType.OPEN_PARENTHESIS) {
        if (inArguments) {
          throw new UnexpectedTokenException(token, "None");
        }
        inArguments = true;
        openParentheses++;
        continue;
      }

      if (type == TokenSyntaxType.CLOSE_PARENTHESIS) {
        if (!inArguments) {
          throw new UnexpectedTokenException(token, "None");
        }
        if (openParentheses == 0) {
          throw new UnexpectedTokenException(token, ")");
        }
        inArguments = false;
        openParentheses--;
        if (!currentArgument.isEmpty()) {
          argumentTokens.add(currentArgument);
          currentArgument = new ArrayList<>();
        }
        continue;
      }

      if (inArguments) {
        if (type == TokenSyntaxType.COMMA) {
          if (currentArgument.isEmpty()) {
            throw new SyntaxException("Comma without preceding argument.");
          }
          argumentTokens.add(currentArgument);
          currentArgument = new ArrayList<>();
        } else {
          currentArgument.add(token);
        }
      }
    }

    if (openParentheses != 0) {
      throw new SyntaxException("Unmatched '(' encountered.");
    }

    if (!currentArgument.isEmpty()) {
      argumentTokens.add(currentArgument);
    }

    return argumentTokens;
  }
}
