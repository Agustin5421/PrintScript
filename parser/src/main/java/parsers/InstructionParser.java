package parsers;

import ast.root.AstNode;
import java.util.List;
import token.Token;

public interface InstructionParser {
  AstNode parse(List<Token> tokens);

  boolean shouldParse(List<Token> tokens);
}
