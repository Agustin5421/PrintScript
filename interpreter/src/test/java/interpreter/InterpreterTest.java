package interpreter;

import ast.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterpreterTest {
    @Test
    public void testExecuteProgram() {
        Identifier identifier = new Identifier("x");
        Literal<String> literal = new StringLiteral("this is a string");
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);
        List<ASTNode> statements = List.of(variableDeclaration);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository =  interpreter.executeProgram(program);

        assertEquals("this is a string", repository.getStringVariable("x"));
    }

    @Test
    public void testExecuteProgramWithNumber() {
        Identifier identifier = new Identifier("x");
        Literal<Number> literal = new NumberLiteral(42);
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);
        List<ASTNode> statements = List.of(variableDeclaration);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository =  interpreter.executeProgram(program);

        assertEquals(42, repository.getNumberVariable("x"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatements() {
        Identifier identifier1 = new Identifier("x");
        Literal<String> literal1 = new StringLiteral("this is a string");
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1);

        Identifier identifier2 = new Identifier("y");
        Literal<Number> literal2 = new NumberLiteral(42);
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2);

        List<ASTNode> statements = List.of(variableDeclaration1, variableDeclaration2);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository =  interpreter.executeProgram(program);

        assertEquals("this is a string", repository.getStringVariable("x"));
        assertEquals(42, repository.getNumberVariable("y"));
    }

    @Test
    public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
        Identifier identifier1 = new Identifier("x");
        Literal<String> literal1 = new StringLiteral("this is a string");
        VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1);

        Identifier identifier2 = new Identifier("x");
        Literal<Number> literal2 = new NumberLiteral(42);
        VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2);

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
        VariablesRepository repository =  interpreter.executeProgram(program);

        assertEquals(0, repository.getStringVars().size());
        assertEquals(0, repository.getNumberVars().size());
    }
}
