package parsers.statements;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.Statement;
import exceptions.SyntaxException;
import java.util.ArrayList;
import java.util.List;
import parsers.Parser;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;
import token.types.TokenType;

public class CallFunctionParser implements StatementParser {
  private final List<String> reservedWords;

  public CallFunctionParser() {
    reservedWords = List.of("println");
  }

  @Override
  public Statement parse(Parser parser, List<Token> tokens) {
    String functionName = tokens.get(0).value();
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();

    // Function name
    Identifier identifier = new Identifier(functionName, start, end);

    // Arguments
    List<Token> subList = tokens.subList(1, tokens.size());
    List<Token> arguments = extractArguments(subList);

    List<AstNode> argumentExpressions = new ArrayList<>();
    for (Token token : arguments) {
      AstNode argument = parser.parseExpression(List.of(token));
      argumentExpressions.add(argument);
    }

    boolean optionalParameters =
        false; // TODO: Implement optional parameters in the future correctly

    return new CallExpression(identifier, argumentExpressions, optionalParameters, start, end);
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return reservedWords.contains(tokens.get(0).value());
  }

  public static List<Token> extractArguments(List<Token> tokens) {
    List<Token> argumentTokens = new ArrayList<>();
    List<Token> currentArgument = new ArrayList<>();
    boolean inArguments = false;
    int openParentheses = 0;

    for (Token token : tokens) {
      TokenType type = token.nodeType();

      if (type == TokenSyntaxType.OPEN_PARENTHESIS) {
        if (inArguments) {
          throw new SyntaxException("Unexpected '(' while already inside arguments.");
        }
        inArguments = true;
        openParentheses++;
        continue;
      }

      if (type == TokenSyntaxType.CLOSE_PARENTHESIS) {
        if (!inArguments) {
          throw new SyntaxException("Unexpected ')' outside of arguments.");
        }
        if (openParentheses == 0) {
          throw new SyntaxException("Unmatched ')' encountered.");
        }
        inArguments = false;
        openParentheses--;
        if (!currentArgument.isEmpty()) {
          argumentTokens.addAll(currentArgument);
          currentArgument.clear();
        }
        continue;
      }

      if (inArguments) {
        if (type == TokenSyntaxType.COMMA) {
          if (currentArgument.isEmpty()) {
            throw new SyntaxException("Comma without preceding argument.");
          }
          argumentTokens.addAll(currentArgument);
          currentArgument.clear();
        } else {
          currentArgument.add(token);
        }
      }
    }

    if (openParentheses != 0) {
      throw new SyntaxException("Unmatched '(' encountered.");
    }

    if (!currentArgument.isEmpty()) {
      argumentTokens.addAll(currentArgument);
    }

    return argumentTokens;
  }
}
