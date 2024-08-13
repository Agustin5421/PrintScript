package ast;

import ast.literal.Literal;
import token.Position;

import java.util.List;

public record CallExpression(Identifier callee, List<Literal> arguments, Position start, Position end) implements Expression {
}
