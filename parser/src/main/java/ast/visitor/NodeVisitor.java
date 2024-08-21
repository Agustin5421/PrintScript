package ast.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

public interface NodeVisitor {
  NodeVisitor visit(CallExpression callExpression);

  NodeVisitor visit(AssignmentExpression assignmentExpression);

  NodeVisitor visit(VariableDeclaration variableDeclaration);

  NodeVisitor visit(NumberLiteral numberLiteral);

  NodeVisitor visit(StringLiteral stringLiteral);

  NodeVisitor visit(Identifier identifier);

  NodeVisitor visit(BinaryExpression binaryExpression);
}
