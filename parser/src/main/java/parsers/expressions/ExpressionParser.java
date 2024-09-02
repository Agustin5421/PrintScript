package parsers.expressions;

import ast.expressions.Expression;
import java.util.List;
import parsers.InstructionParser;
import parsers.Parser;
import token.Token;

public interface ExpressionParser extends InstructionParser {
  Expression parse(Parser parser, List<Token> tokens);
}
