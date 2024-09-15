package interpreter.rework;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.InterpreterVisitorV3Factory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import token.Position;

public class VisitorV1Test extends CommonVisitorV3Test {
  private InterpreterVisitorV3 visitor;

  @BeforeEach
  public void setUp() {
    visitor = InterpreterVisitorV3Factory.getVisitor("1.0", new OutputListString());
  }

  @Override
  protected InterpreterVisitorV3 getVisitor() {
    return visitor;
  }

  @Test
  public void failWithIfStatement() {
    Position position = new Position(0, 0);
    Identifier condition = new Identifier("x", position, position);
    IfStatement ifStatement = new IfStatement(null, null, condition, List.of(), List.of());

    InterpreterVisitorV3 visitor = getVisitor();
    assertThrows(Exception.class, () -> visitor.visit(ifStatement));
  }

  @Test
  public void failWithBooleanLiteral() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, booleanLiteral, "string", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    assertThrows(Exception.class, () -> visitor.visit(variableDeclaration));
  }
}
