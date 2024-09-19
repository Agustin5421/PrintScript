package strategy;

import ast.root.AstNode;

public interface Strategy<T> {
  T apply(AstNode node, T engine);
}
