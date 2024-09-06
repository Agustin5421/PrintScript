package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorV1;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterTest {
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void testExecuteProgram() {
    Identifier identifier = new Identifier("x", new Position(0, 0), new Position(0, 1));
    Literal<String> literal =
        new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);
    List<AstNode> statements = List.of(variableDeclaration);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals("this is a string", repository.getVariable(identifier).value());
  }

  @Test
  public void testExecuteProgramWithNumber() {
    Identifier identifier = new Identifier("x", new Position(0, 0), new Position(0, 1));
    Literal<Number> literal = new NumberLiteral(42, new Position(2, 0), new Position(2, 3));
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);
    List<AstNode> statements = List.of(variableDeclaration);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(42, repository.getVariable(identifier).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatements() {
    Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
    Literal<String> literal1 =
        new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));

    VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1);

    Identifier identifier2 = new Identifier("y", new Position(6, 0), new Position(6, 7));
    Literal<Number> literal2 = new NumberLiteral(42, new Position(8, 0), new Position(8, 9));
    VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2);

    List<AstNode> statements = List.of(variableDeclaration1, variableDeclaration2);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals("this is a string", repository.getVariable(identifier1).value());
    assertEquals(42, repository.getVariable(identifier2).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
    Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
    Literal<String> literal1 =
        new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
    List<AstNode> statements = getAstNodes(identifier1, literal1, "x");
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          interpreter.executeProgram(program);
        });
  }

  private static List<AstNode> getAstNodes(
      Identifier identifier1, Literal<String> literal1, String x) {
    VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1);

    Identifier identifier2 = new Identifier(x, new Position(6, 0), new Position(6, 7));
    Literal<Number> literal2 = new NumberLiteral(42, new Position(8, 0), new Position(8, 9));
    VariableDeclaration variableDeclaration2 = new VariableDeclaration(identifier2, literal2);

    List<AstNode> statements = List.of(variableDeclaration1, variableDeclaration2);
    return statements;
  }

  @Test
  public void testExecuteEmptyProgram() {
    Program program = new Program(List.of(), new Position(0, 0), new Position(0, 1));

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(0, repository.getVariables().size());
  }

  @Test
  public void testPrinting() {
    Identifier identifier1 = new Identifier("x", new Position(0, 0), new Position(0, 1));
    Literal<String> literal1 =
        new StringLiteral("this is a string", new Position(2, 0), new Position(2, 3));
    VariableDeclaration variableDeclaration1 = new VariableDeclaration(identifier1, literal1);

    VariablesRepository repository = addPrintStatement(variableDeclaration1);

    assertEquals("this is a string", repository.getVariable(identifier1).value());
  }

  @Test
  public void testBinaryExpressionPrinting() {
    Identifier identifier = new Identifier("x", defaultPosition, defaultPosition);
    Literal<Number> literal = new NumberLiteral(42.5, defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);

    Identifier identifier2 = new Identifier("y", defaultPosition, defaultPosition);
    BinaryExpression binaryExpression =
        new BinaryExpression(
            identifier, new NumberLiteral(42.5, defaultPosition, defaultPosition), "+");
    VariableDeclaration variableDeclaration2 =
        new VariableDeclaration(identifier2, binaryExpression);

    Identifier printName = new Identifier("println", new Position(6, 0), new Position(6, 6));
    List<AstNode> arguments = List.of(new Identifier("y", new Position(8, 0), new Position(8, 1)));
    CallExpression callExpression =
        new CallExpression(printName, arguments, false, new Position(6, 0), new Position(6, 6));

    List<AstNode> arguments1 = List.of(new Identifier("x", new Position(8, 0), new Position(8, 1)));
    CallExpression callExpression1 =
        new CallExpression(printName, arguments1, false, new Position(6, 0), new Position(6, 6));

    List<AstNode> statements =
        List.of(variableDeclaration, variableDeclaration2, callExpression, callExpression1);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(42.5, repository.getVariable(identifier).value());
    assertEquals(85.0, repository.getVariable(identifier2).value());
  }

  @Test
  public void testBinaryExpressionPrintForReal() {
    Identifier identifier = new Identifier("x", defaultPosition, defaultPosition);
    Literal<Number> literal = new NumberLiteral(42.5, defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);

    BinaryExpression binaryExpression =
        new BinaryExpression(
            identifier, new NumberLiteral(42.5, defaultPosition, defaultPosition), "+");
    Identifier printName = new Identifier("println", new Position(6, 0), new Position(6, 6));
    CallExpression callExpression =
        new CallExpression(
            printName, List.of(binaryExpression), false, new Position(6, 0), new Position(6, 6));

    List<AstNode> statements = List.of(variableDeclaration, callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);
  }

  @Test
  public void testBinaryStringSumPrint() {
    BinaryExpression binaryExpression =
        new BinaryExpression(
            new StringLiteral("Hello ", defaultPosition, defaultPosition),
            new StringLiteral("World", defaultPosition, defaultPosition),
            "+");
    Identifier printName = new Identifier("println", new Position(6, 0), new Position(6, 6));
    CallExpression callExpression =
        new CallExpression(
            printName, List.of(binaryExpression), false, new Position(6, 0), new Position(6, 6));

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);
  }

  @Test
  public void testBinaryExpressionStringAndNumberPrint() {
    Identifier identifier = new Identifier("x", defaultPosition, defaultPosition);
    Literal<Number> literal = new NumberLiteral(42.5, defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);

    BinaryExpression binaryExpression =
        new BinaryExpression(
            new StringLiteral("a", defaultPosition, defaultPosition), identifier, "+");
    Identifier printName = new Identifier("println", new Position(6, 0), new Position(6, 6));
    CallExpression callExpression =
        new CallExpression(
            printName, List.of(binaryExpression), false, new Position(6, 0), new Position(6, 6));

    List<AstNode> statements = List.of(variableDeclaration, callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);
  }

  private VariablesRepository addPrintStatement(VariableDeclaration variableDeclaration) {
    Identifier methodName = new Identifier("println", new Position(6, 0), new Position(6, 6));
    List<AstNode> arguments = List.of(new Identifier("x", new Position(8, 0), new Position(8, 1)));
    CallExpression callExpression =
        new CallExpression(methodName, arguments, false, new Position(6, 0), new Position(6, 6));

    List<AstNode> statements = List.of(variableDeclaration, callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    return interpreter.executeProgram(program);
  }

  @Test
  public void testExecuteProgramWithBinaryExpression() {
    Identifier identifier = new Identifier("x", defaultPosition, defaultPosition);
    Literal<Number> literal = new NumberLiteral(42.5, defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, literal);

    Identifier identifier2 = new Identifier("y", defaultPosition, defaultPosition);
    BinaryExpression binaryExpression =
        new BinaryExpression(
            identifier, new NumberLiteral(42.5, defaultPosition, defaultPosition), "+");
    VariableDeclaration variableDeclaration2 =
        new VariableDeclaration(identifier2, binaryExpression);

    List<AstNode> statements = List.of(variableDeclaration, variableDeclaration2);
    Program program = new Program(statements);

    InterpreterVisitor visitor = new InterpreterVisitorV1(new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);

    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(42.5, repository.getVariable(identifier).value());
    assertEquals(85.0, repository.getVariable(identifier2).value());
  }
}
