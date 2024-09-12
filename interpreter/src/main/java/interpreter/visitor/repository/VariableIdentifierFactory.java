package interpreter.visitor.repository;

import ast.identifier.Identifier;
import ast.statements.VariableDeclaration;

public class VariableIdentifierFactory {
  public static VariableIdentifier createVarIdFromVarDec(VariableDeclaration variableDeclaration) {
    String name = variableDeclaration.identifier().name();
    String type = variableDeclaration.varType();
    boolean isModifiable = variableDeclaration.kind().equals("let");

    return new VariableIdentifier(name, type, isModifiable);
  }

  public static VariableIdentifier createVarIdFromIdentifier(Identifier identifier) {
    String name = identifier.name();

    return new VariableIdentifier(name);
  }
}
