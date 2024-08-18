package visitors;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNode;

import java.util.List;

public interface ASTVisitor {
    ASTVisitor visitVarDec(Identifier identifier, Expression expression);
    ASTVisitor visitCallExpression(Identifier identifier, List<ASTNode> arguments);
    ASTVisitor visitAssignmentExpression(Identifier identifier, String operator, Expression expression);
}
