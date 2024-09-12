package formatter.visitor;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import formatter.strategy.FormattingStrategy;
import java.util.Map;

public interface FormatterVisitor extends NodeVisitor {
  String getCurrentCode();

  int getValue();

  Map<AstNodeType, FormattingStrategy> getStrategies();

  FormattingStrategy getStrategy(AstNode node) throws IllegalArgumentException;

  FormatterVisitor newVisitor(String newCode);

  FormatterVisitor cloneVisitor();
}
