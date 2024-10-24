package program;

import ast.root.AstNode;
import java.util.List;

public record ProgramNode(List<AstNode> nodes) {}
