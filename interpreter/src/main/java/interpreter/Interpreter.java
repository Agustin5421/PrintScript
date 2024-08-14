package interpreter;

import ast.*;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import java.util.List;
import static ast.records.StatementValidator.*;

public class Interpreter {
    public VariablesRepository executeProgram(Program program) {
        VariablesRepository variablesRepository = new VariablesRepository();

        for (ASTNode statement : program.statements()) {
            variablesRepository = evaluateStatement(statement, variablesRepository);
        }

        return variablesRepository;
    }

    private VariablesRepository evaluateStatement(ASTNode statement, VariablesRepository variablesRepository) {
        if (isVariableDeclaration(statement)) {
            VariableDeclaration variableDeclaration = (VariableDeclaration) statement;

            String name = variableDeclaration.identifier().name();
            Literal<?> literal = variableDeclaration.literal();
            Object value = literal.value();

            return variablesRepository.addVariable(name, value);
        } else if (isCallExpression(statement)) {
            CallExpression callExpression = (CallExpression) statement;
            Identifier identifier = callExpression.methodIdentifier();
            List<Expression> arguments = callExpression.arguments();
            boolean optionalParameters = callExpression.optionalParameters();

            if (identifier.name().equals("println")) {
                for (Expression argument : arguments) {
                    if (argument instanceof StringLiteral stringLiteral) {
                        System.out.print(stringLiteral.value());
                    } else if (argument instanceof NumberLiteral numberLiteral) {
                        System.out.print(numberLiteral.value());
                    } else if (argument instanceof Identifier identifierArgument) {
                        System.out.print(variablesRepository.getVariable(identifierArgument.name()));
                    }
                }
                System.out.println();
            }

            return variablesRepository;
        }
//        else if (isExpressionStatement(statement)) {
//            ExpressionStatement expressionStatement = (ExpressionStatement) statement;
//            ExpressionType expressionType = expressionStatement.expressionType();
//
//            if (isCallExpression(expressionType)) {
//                CallExpression callExpression = (CallExpression) expressionType;
//                Identifier identifier = callExpression.identifier();
//                List<Expression> arguments = callExpression.arguments();
//                boolean optionalParameters = callExpression.optionalParameters();
//
//                if (identifier.getName().equals("println")) {
//                    for (Expression argument : arguments) {
//                        if (argument instanceof StringLiteral) {
//                            StringLiteral stringLiteral = (StringLiteral) argument;
//                            System.out.print(stringLiteral.value());
//                        } else if (argument instanceof NumberLiteral) {
//                            NumberLiteral numberLiteral = (NumberLiteral) argument;
//                            System.out.print(numberLiteral.value());
//                        } else if (argument instanceof Identifier) {
//                            Identifier identifierArgument = (Identifier) argument;
//                            System.out.print(variablesRepository.getVariable(identifierArgument.getName()));
//                        }
//                    }
//                    System.out.println();
//                }
//
//                return variablesRepository;
//            }
//        }
        return variablesRepository;
    }
}
