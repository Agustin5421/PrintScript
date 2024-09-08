package parsers.expressions;

import ast.expressions.ExpressionNode;
import java.util.List;
import parsers.InstructionParser;
import parsers.Parser;
import token.Token;

public interface ExpressionParser extends InstructionParser {
  ExpressionNode parse(Parser parser, List<Token> tokens);
}
