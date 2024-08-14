package interpreter;

import ast.*;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import org.junit.jupiter.api.Test;
import token.Position;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterpreterTest {
    private final Position defaultPosition = new Position(0,0);
    @Test
    public void testExecuteProgram() {
        Identifier identifier = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal, new Position(4, 0), new Position(4, 5));
        List<ASTNode> statements = List.of(variableDeclaration);
        Program program = new Program(statements, new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals("this is a string", repository.getVariable("x"));
    }

    @Test
    public void testExecuteProgramWithNumber() {
        Identifier identifier = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<Number> literal = new NumberLiteral(42, new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal, new Position(4, 0), new Position(4, 5));
        List<ASTNode> statements = List.of(variableDeclaration);
        Program program = new Program(statements, new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(42, repository.getVariable("x"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatements() {
        Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal1 = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        List<ASTNode> statements = getAstNodes(identifier1, literal1, "y");
        Program program = new Program(statements,new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals("this is a string", repository.getVariable("x"));
        assertEquals(42, repository.getVariable("y"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
        Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal1 = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        List<ASTNode> statements = getAstNodes(identifier1, literal1, "x");
        Program program = new Program(statements, new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();

        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.executeProgram(program);
        });
    }

    private static List<ASTNode> getAstNodes(Identifier identifier1, Literal<String> literal1, String x) {
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1, new Position(4, 0), new Position(4, 5));

        Identifier identifier2 = new Identifier(x, new Position(6, 0), new Position(6, 7));
        Literal<Number> literal2 = new NumberLiteral(42, new Position(8, 0), new Position(8, 9));
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2, new Position(10, 0), new Position(10, 11));

        List<ASTNode> statements = List.of(variableDeclaration1, variableDeclaration2);
        return statements;
    }

    @Test
    public void testExecuteEmptyProgram() {
        Program program = new Program(List.of(),new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(0, repository.getStringVars().size());
        assertEquals(0, repository.getNumberVars().size());
    }

    @Test
    public void testPrinting() {
        Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal1 = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1, new Position(4, 0), new Position(4, 5));

        VariablesRepository repository = getVariablesRepository(variableDeclaration1);

        assertEquals("this is a string", repository.getVariable("x"));
    }

    private static VariablesRepository getVariablesRepository(VariableDeclaration variableDeclaration1) {
        Identifier methodName = new Identifier("println", new Position(6, 0), new Position(6, 6));
        List<Expression> arguments = List.of(new Identifier("x", new Position(8, 0), new Position(8, 1)));
        CallExpression callExpression = new CallExpression(methodName, arguments, false, new Position(6, 0), new Position(6, 6));

        List<ASTNode> statements = List.of(variableDeclaration1, callExpression);
        Program program = new Program(statements, new Position(0, 0), new Position(0, 1));

        Interpreter interpreter = new Interpreter();
        return interpreter.executeProgram(program);
    }

    @Test
    public void testExecuteProgramWithBinaryExpression() {
        Identifier identifier = new Identifier("x", defaultPosition, defaultPosition);
        Literal<Number> literal = new NumberLiteral(42, defaultPosition, defaultPosition);
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal, defaultPosition, defaultPosition);

        Identifier identifier2 = new Identifier("y", defaultPosition, defaultPosition);
        BinaryExpression binaryExpression = new BinaryExpression(identifier, new NumberLiteral(42, defaultPosition, defaultPosition), "+");
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, binaryExpression, defaultPosition, defaultPosition);

        List<ASTNode> statements = List.of(variableDeclaration, variableDeclaration2);
        Program program = new Program(statements, defaultPosition, defaultPosition);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository =  interpreter.executeProgram(program);

        assertEquals(42, repository.getVariable("x"));
        assertEquals(84, repository.getVariable("y"));
    }
}