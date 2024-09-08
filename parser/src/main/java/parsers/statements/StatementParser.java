package parsers.statements;

import ast.statements.StatementNode;
import java.util.List;
import parsers.InstructionParser;
import parsers.Parser;
import token.Token;

public interface StatementParser extends InstructionParser {
  StatementNode parse(Parser parser, List<Token> tokens);
}
