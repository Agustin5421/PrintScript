package parsers.statements;

import ast.statements.Statement;
import java.util.List;
import parsers.InstructionParser;
import parsers.Parser;
import token.Token;

public interface StatementParser extends InstructionParser {
  Statement parse(Parser parser, List<Token> tokens);
}
