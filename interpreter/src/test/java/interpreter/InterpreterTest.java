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
    @Test
    public void testExecuteProgram() {
        Identifier identifier = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal, new Position(4, 0), new Position(4, 5));
        List<ASTNode> statements = List.of(variableDeclaration);
        Program program = new Program(statements);

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
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(42, repository.getVariable("x"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatements() {
        Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal1 = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1, new Position(4, 0), new Position(4, 5));

        Identifier identifier2 = new Identifier("y", new Position(6, 0), new Position(6, 7));
        Literal<Number> literal2 = new NumberLiteral(42, new Position(8, 0), new Position(8, 9));
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2, new Position(10, 0), new Position(10, 11));

        List<ASTNode> statements = List.of(variableDeclaration1, variableDeclaration2);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals("this is a string", repository.getVariable("x"));
        assertEquals(42, repository.getVariable("y"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
        Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
        Literal<String> literal1 = new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1, new Position(4, 0), new Position(4, 5));

        Identifier identifier2 = new Identifier("x", new Position(6, 0), new Position(6, 7));
        Literal<Number> literal2 = new NumberLiteral(42, new Position(8, 0), new Position(8, 9));
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2, new Position(10, 0), new Position(10, 11));

        List<ASTNode> statements = List.of(variableDeclaration1, variableDeclaration2);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();

        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.executeProgram(program);
        });
    }

    @Test
    public void testExecuteEmptyProgram() {
        Program program = new Program(List.of());

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(0, repository.getStringVars().size());
        assertEquals(0, repository.getNumberVars().size());
    }
}