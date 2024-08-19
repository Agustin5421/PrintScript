package linter;

import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.Program;
import ast.statements.VariableDeclaration;
import org.junit.jupiter.api.Test;
import token.Position;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LinterTest {
    private final Position defaultPosition = new Position(0, 0);

    @Test
    public void testIdentifierRuleIsMet() {
        Linter linter = LinterInitializer.initLinter();
        String rules = TestUtils.readResourceFile("linterRulesExample.json");
        assertNotNull(rules);

        Identifier identifier = new Identifier("testName", defaultPosition, defaultPosition);
        StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
        Program program = new Program(List.of(variableDeclaration));

        String report = linter.linter(program, rules);
        assertEquals("", report);
    }

    @Test
    public void testIdentifierRuleIsNotMet() {
        Linter linter = LinterInitializer.initLinter();
        String rules = TestUtils.readResourceFile("linterRulesExample.json");
        assertNotNull(rules);

        Identifier identifier = new Identifier("test_name", defaultPosition, defaultPosition);
        StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
        Program program = new Program(List.of(variableDeclaration));

        String report = linter.linter(program, rules);
        assertEquals("Warning from (row: 0, col: 0) to (row: 0, col: 0): Identifier test_name does not follow camelCase convention", report);
    }

    @Test
    public void testIdentifierRuleIsNotMet2() {
        Linter linter = LinterInitializer.initLinter();
        String rules = TestUtils.readResourceFile("linterRulesExample.json");
        assertNotNull(rules);

        Identifier identifier = new Identifier("TestName", defaultPosition, defaultPosition);
        StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
        VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
        Program program = new Program(List.of(variableDeclaration));

        String report = linter.linter(program, rules);
        assertEquals("Warning from (row: 0, col: 0) to (row: 0, col: 0): Identifier TestName does not follow camelCase convention", report);
    }
}
