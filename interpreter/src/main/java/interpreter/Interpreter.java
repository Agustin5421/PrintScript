package interpreter;

import ast.Expression;
import ast.Literal;
import ast.Program;
import ast.ASTNode;
import interpreter.runtime.BinaryExpressionEvaluator;

public class Interpreter {
    public VariablesRepository executeProgram(Program program) {
        VariablesRepository variablesRepository = new VariablesRepository();

        for (ASTNode statement : program.getStatements()) {
            variablesRepository = evaluateStatement(statement, variablesRepository);
        }

        return variablesRepository;
    }

    private VariablesRepository evaluateStatement(ASTNode statement, VariablesRepository variablesRepository) {
        if (statement instanceof ast.VariableDeclaration) {
            ast.VariableDeclaration variableDeclaration = (ast.VariableDeclaration) statement;

            String name = variableDeclaration.getIdentifier().getName();
            Literal literal = variableDeclaration.getLiteral();
            Object value = literal.getValue();

            return variablesRepository.addVariable(name, value);
        } else if (statement instanceof ast.BinaryExpression)  {
            BinaryExpressionEvaluator binaryExpression = new BinaryExpressionEvaluator();
            binaryExpression.evaluate((Expression) statement);
        }
        return variablesRepository;
    }
}
