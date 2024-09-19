package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;

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
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;

    // Adding the let or const keyword
    String kind = varDecNode.kind();
    engine.write(kind);
    engine.write(keyWordSpace);

    // Adding the identifier
    engine.format(varDecNode.identifier());

    // Adding the whitespaces strategies for " : type"
    typeAssignation.apply(varDecNode, engine);

    // Assigning the expression
    assignationStrategy.apply(varDecNode.expression(), engine);

    engine.write(";\n");

    return engine;
  }
}
