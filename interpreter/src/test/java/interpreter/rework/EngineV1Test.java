package interpreter.rework;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.factory.InterpreterEngineFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import position.Position;

public class EngineV1Test extends CommonEngineTest {
  private InterpreterEngine engine;

  @BeforeEach
  public void setUp() {
    engine = InterpreterEngineFactory.getEngine("1.0", new OutputListString());
  }

  @Override
  protected InterpreterEngine getEngine() {
    return engine;
  }

  @Test
  public void failWithIfStatement() {
    Position position = new Position(0, 0);
    Identifier condition = new Identifier("x", position, position);
    IfStatement ifStatement = new IfStatement(null, null, condition, List.of(), List.of());

    InterpreterEngine visitor = getEngine();
    assertThrows(Exception.class, () -> visitor.interpret(ifStatement));
  }

  @Test
  public void failWithBooleanLiteral() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, booleanLiteral, "string", position, position);

    InterpreterEngine visitor = getEngine();
    assertThrows(Exception.class, () -> visitor.interpret(variableDeclaration));
  }

  @Test
  public void failWithReadMethods() {
    Position position = new Position(0, 0);
    Identifier readInput = new Identifier("readInput", position, position);
    Identifier readEnv = new Identifier("readEnv", position, position);
    StringLiteral stringLiteral = new StringLiteral("Sample text", position, position);
    CallExpression inputCall = new CallExpression(readInput, List.of(), position, position);
    CallExpression envCall =
        new CallExpression(readEnv, List.of(stringLiteral), position, position);
    List<AstNode> statements = List.of(inputCall, envCall);

    InterpreterEngine visitor = getEngine();

    for (AstNode statement : statements) {
      assertThrows(Exception.class, () -> visitor.interpret(statement));
    }
  }
}
