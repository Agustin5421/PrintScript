package visitors;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.ASTNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

import java.util.List;

public interface ASTVisitor {
    ASTVisitor visitVarDec(VariableDeclaration node);
    ASTVisitor visitCallExpression(CallExpression node);
    ASTVisitor visitAssignmentExpression(AssignmentExpression node);
    ASTVisitor visitIdentifier(Identifier node);
    ASTVisitor visitLiteral(Literal<?> node);
}
