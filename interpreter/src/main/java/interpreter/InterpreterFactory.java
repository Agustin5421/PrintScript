package interpreter;

import interpreter.engine.InterpreterEngine;
import interpreter.engine.factory.InterpreterEngineFactory;
import output.OutputResult;

public class InterpreterFactory {
  public static Interpreter buildInterpreter(String version, OutputResult<String> outputResult) {
    InterpreterEngine interpreterEngine = InterpreterEngineFactory.getEngine(version, outputResult);

    return new Interpreter(interpreterEngine);
  }
}
