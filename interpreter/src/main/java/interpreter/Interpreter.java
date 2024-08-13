package interpreter;

import ast.*;
import ast.expressions.CallExpression;
import ast.expressions.ExpressionStatement;
import ast.expressions.ExpressionType;

import java.util.List;

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
        } else if (statement instanceof ExpressionStatement) {
            ExpressionStatement expressionStatement = (ExpressionStatement) statement;
            ExpressionType expressionType = expressionStatement.getExpressionType();

            if (expressionType instanceof CallExpression) {
                CallExpression callExpression = (CallExpression) expressionType;
                Identifier identifier = callExpression.getIdentifier();
                List<Expression> arguments = callExpression.getArguments();
                boolean optionalParameters = callExpression.hasOptionalParameters();

                if (identifier.getName().equals("println")) {
                    for (Expression argument : arguments) {
                        if (argument instanceof StringLiteral) {
                            StringLiteral stringLiteral = (StringLiteral) argument;
                            System.out.print(stringLiteral.getValue());
                        } else if (argument instanceof NumberLiteral) {
                            NumberLiteral numberLiteral = (NumberLiteral) argument;
                            System.out.print(numberLiteral.getValue());
                        } else if (argument instanceof Identifier) {
                            Identifier identifierArgument = (Identifier) argument;
                            System.out.print(variablesRepository.getVariable(identifierArgument.getName()));
                        }
                    }
                    System.out.println();
                }

                return variablesRepository;
            }
        }
        return variablesRepository;
    }
}
