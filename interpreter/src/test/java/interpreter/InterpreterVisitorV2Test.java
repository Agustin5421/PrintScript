package interpreter;

import ast.identifier.Identifier;

import ast.root.AstNode;
import ast.root.Program;
import ast.statements.CallExpression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import token.Position;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterVisitorV2Test {

    @Test
    public void testReadInputNumberDouble() {
        String input = "42.0";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of( callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(42.0, repository.getVariable(methodIdentifier).value());
    }

    @Test
    public void testReadInputNumberInt() {
        String input = "42";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of( callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(42, repository.getVariable(methodIdentifier).value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "TRUE", "t", "T", "false", "FALSE", "f", "F"})
    public void testReadInputBoolean(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of(callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        boolean expectedValue = input.equalsIgnoreCase("true") || input.equalsIgnoreCase("t");
        assertEquals(expectedValue, repository.getVariable(methodIdentifier).value());
    }

    @Test
    public void testReadInputString() {
        String input = "hello";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of(callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals("hello", repository.getVariable(methodIdentifier).value());
    }

    @Test
    public void testReadInputStringSpace() {
        String input = " ";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of(callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals(" ", repository.getVariable(methodIdentifier).value());
    }

    @Test
    public void testReadInputStringWithNothing() {
        String input = "\nvalidInput";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Position defaultPosition = new Position(0, 0);
        Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
        CallExpression callExpression = new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

        List<AstNode> statements = List.of(callExpression);
        Program program = new Program(statements);

        Interpreter interpreter = new Interpreter();
        VariablesRepository repository = interpreter.executeProgram(program);

        assertEquals("validInput", repository.getVariable(methodIdentifier).value());
    }
}