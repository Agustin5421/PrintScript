package parsers;

import ast.ASTNode;

import token.Token;

import java.util.List;

public interface InstructionParser {
    ASTNode parse(List<Token> tokens);
    boolean shouldParse(List<Token> tokens);
}
