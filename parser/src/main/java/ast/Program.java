package ast;

import token.Position;

import java.util.List;

public record Program(List<ASTNode> statements, Position start, Position end) {
}
