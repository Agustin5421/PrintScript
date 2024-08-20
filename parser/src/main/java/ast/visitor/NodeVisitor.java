package ast.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

public interface NodeVisitor {
    void visit(CallExpression callExpression);
    void visit(AssignmentExpression assignmentExpression);
    void visit(VariableDeclaration variableDeclaration);
    void visit(NumberLiteral numberLiteral);
    void visit(StringLiteral stringLiteral);
    void visit(Identifier identifier);

    void visit(BinaryExpression binaryExpression);
}
