package parsers;

import java.util.List;
import token.Token;

public interface InstructionParser {
  boolean shouldParse(List<Token> tokens);
}
