package parsers;

import ast.root.ASTNode;
import java.util.List;
import token.Token;

public interface InstructionParser {
  ASTNode parse(List<Token> tokens);

  boolean shouldParse(List<Token> tokens);
}
