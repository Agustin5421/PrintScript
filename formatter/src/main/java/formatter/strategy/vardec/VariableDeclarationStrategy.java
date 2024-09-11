package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.visitor.FormatterVisitor;

public class VariableDeclarationStrategy implements FormattingStrategy {
  // Strategies should be having (or not) the first space for : and the
  // second space, then the type of the variable
  private final TypingStrategy typeAssignation;
  private final String keyWordSpace;
  private final AssignationStrategy assignationStrategy;

  public VariableDeclarationStrategy(
      TypingStrategy typeAssignation,
      String keyWordSpace,
      AssignationStrategy assignationStrategy) {
    this.typeAssignation = typeAssignation;
    this.keyWordSpace = keyWordSpace;
    this.assignationStrategy = assignationStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    StringBuilder formattedCode = new StringBuilder();

    // Adding the let or const keyword
    String kind = varDecNode.kind();
    formattedCode.append(kind).append(keyWordSpace);

    // Adding the identifier
    FormatterVisitor visitIdentifier = (FormatterVisitor) varDecNode.identifier().accept(visitor);
    formattedCode.append(visitIdentifier.getCurrentCode());

    // Adding the whitespaces strategies for " : type"
    formattedCode.append(typeAssignation.apply(varDecNode, visitor));

    // Assigning the expression
    formattedCode.append(assignationStrategy.apply(varDecNode.expression(), visitor));
    formattedCode.append(";");

    return formattedCode.toString();
  }
}
