package interpreter;

import ast.root.AstNode;
import interpreter.engine.InterpreterEngine;

public record Interpreter(InterpreterEngine interpreterEngine) {
  public Interpreter interpretNext(AstNode nodeToInterpret) {
    InterpreterEngine afterInterpretation = interpreterEngine.interpret(nodeToInterpret);

    return new Interpreter(afterInterpretation);
  }
}
