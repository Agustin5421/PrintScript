package interpreter;

import ast.Expression;
import ast.Literal;
import ast.Program;
import ast.ASTNode;
import interpreter.runtime.ExpressionEvaluator;

public class Interpreter {
    public VariablesRepository executeProgram(Program program) {
        VariablesRepository variablesRepository = new VariablesRepository();

        for (ASTNode statement : program.getStatements()) {
            variablesRepository = evaluateStatement(statement, variablesRepository);
        }

        return variablesRepository;
    }

    private VariablesRepository evaluateStatement(ASTNode statement, VariablesRepository variablesRepository) {
        if (statement instanceof ast.VariableDeclaration variableDeclaration) {
            String name = variableDeclaration.identifier().getName();
            Expression expression = variableDeclaration.expression();
            ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(variablesRepository);
            Literal<?> value = (Literal<?>) expressionEvaluator.evaluate(expression);

            return variablesRepository.addVariable(name, value);
        } else if (statement instanceof ast.BinaryExpression)  {
            ExpressionEvaluator binaryExpression = new ExpressionEvaluator(variablesRepository);
            binaryExpression.evaluate((Expression) statement);
        }
        return variablesRepository;
    }
}
