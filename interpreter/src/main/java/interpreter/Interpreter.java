package interpreter;

import ast.*;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import interpreter.runtime.ExpressionEvaluator;

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
            return setVariable((VariableDeclaration) statement, variablesRepository);
        } else if (isCallExpression(statement)) {
            CallExpression callExpression = (CallExpression) statement;
            Identifier identifier = callExpression.methodIdentifier();
            List<Expression> arguments = callExpression.arguments();
            boolean optionalParameters = callExpression.optionalParameters(); //TODO: how to use this?

            String name = "println"; //TODO: como hacerlo generico? tal vez un enum con todos los tipos pero no se si es buena idea
            printlnMethod(variablesRepository, identifier, "println", arguments);

            return variablesRepository;
        }

        return variablesRepository;
    }

    private static VariablesRepository setVariable(VariableDeclaration statement, VariablesRepository variablesRepository) {
        String name = statement.identifier().name();
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(variablesRepository, statement.start().row());
        Literal<?> literal = (Literal<?>) expressionEvaluator.evaluate(statement.expression());
        Object value = literal.value();

        return variablesRepository.addVariable(name, value);
    }

    private void printlnMethod(VariablesRepository variablesRepository, Identifier identifier, String name, List<Expression> arguments) {
        if (identifier.name().equals(name)) {
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
    }
}
