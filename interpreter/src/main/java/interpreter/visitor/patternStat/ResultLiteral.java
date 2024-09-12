package interpreter.visitor.patternStat;

import ast.literal.Literal;
import java.util.List;

public record ResultLiteral(Literal<?> literal, List<String> strings) {}
