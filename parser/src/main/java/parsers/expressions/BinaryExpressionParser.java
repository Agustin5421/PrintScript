package parsers.expressions;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.root.AstNode;
import ast.utils.ExpressionParserProvider;
import java.util.List;
import token.Position;
import token.Token;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;

public class BinaryExpressionParser implements ExpressionParser {

  @Override
  public AstNode parse(List<Token> tokens) {
    if (tokens.isEmpty()) {
      throw new IllegalArgumentException("Token list cannot be empty");
    }

    // Remove unnecessary parentheses
    tokens = removeUnnecessaryParentheses(tokens);

    // Find the top-level operator based on precedence
    int operatorIndex = findOperatorByPrecedence(tokens);

    Token operator = tokens.get(operatorIndex);

    // Divide the tokens into left and right expressions
    List<Token> leftTokens = tokens.subList(0, operatorIndex);
    List<Token> rightTokens = tokens.subList(operatorIndex + 1, tokens.size());

    // Recursively parse the left and right expressions
    Expression left = ExpressionParserProvider.parse(leftTokens);
    Expression right = ExpressionParserProvider.parse(rightTokens);

    return new BinaryExpression(left, right, operator.value());
  }

  private List<Token> removeUnnecessaryParentheses(List<Token> tokens) {
    // Eliminate surrounding parentheses only if they wrap the entire expression
    while (tokens.size() > 2
        && tokens.get(0).type() == TokenSyntaxType.OPEN_PARENTHESIS
        && tokens.get(tokens.size() - 1).type() == TokenSyntaxType.CLOSE_PARENTHESIS) {

      int level = 0;
      boolean valid = true;

      for (int i = 0; i < tokens.size(); i++) {
        Token token = tokens.get(i);
        if (token.type() == TokenSyntaxType.OPEN_PARENTHESIS) {
          level++;
        } else if (token.type() == TokenSyntaxType.CLOSE_PARENTHESIS) {
          level--;
        }

        // If we close all parentheses before the end, they aren't just wrapping
        if (level == 0 && i < tokens.size() - 1) {
          valid = false;
          break;
        }
      }

      if (valid && level == 0) {
        tokens = tokens.subList(1, tokens.size() - 1);
      } else {
        break;
      }
    }
    return tokens;
  }

  private int findOperatorByPrecedence(List<Token> tokens) {
    int level = 0;
    int operatorIndex = -1;
    int precedence = Integer.MAX_VALUE;

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);

      if (token.type() == TokenSyntaxType.OPEN_PARENTHESIS) {
        level++;
      } else if (token.type() == TokenSyntaxType.CLOSE_PARENTHESIS) {
        level--;
      } else if (level == 0 && isOperator(token)) {
        int currentPrecedence = getPrecedence(token);

        // Choose the operator with the lowest precedence (leftmost if same precedence)
        if (currentPrecedence < precedence
            || (currentPrecedence == precedence && operatorIndex == -1)) {
          precedence = currentPrecedence;
          operatorIndex = i;
        }
      }
    }

    if (operatorIndex == -1) {
      StringBuilder statementString = new StringBuilder();
      for (Token token : tokens) {
        statementString.append(token.value());
      }

      Position first = tokens.get(0).initialPosition();

      String message = getExceptionMessage(statementString.toString(), first.row(), first.col());

      throw new UnsupportedOperationException("No valid operator found in: " + message);
    }

    return operatorIndex;
  }

  private boolean isOperator(Token token) {
    return token.type() == TokenDataType.OPERAND;
  }

  private int getPrecedence(Token token) {
    // Define precedence for different operators
    return switch (token.value()) {
      case "*", "/" -> 2;
      case "+", "-" -> 1;
      default -> 0;
    };
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    for (Token token : tokens) {
      if (isOperator(token)) {
        return true;
      }
    }
    return false;
  }
}
