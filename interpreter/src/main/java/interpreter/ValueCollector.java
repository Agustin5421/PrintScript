package interpreter;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.StrategyContainer;
import output.OutputResult;

public class ValueCollector implements NodeVisitor {
  private final Literal<?> value;
  // Only works with Identifier, Literals, BinaryExpressions and CallExpression (readInput() and
  // readEnv()).
  private final StrategyContainer<AstNodeType> strategies;
  private final VariablesRepository variablesRepository;

  public ValueCollector(
      StrategyContainer<AstNodeType> strategies,
      Literal<?> value,
      VariablesRepository variablesRepository) {
    this.value = value;
    this.strategies = strategies;
    this.variablesRepository = variablesRepository;
  }

  public ValueCollector(StrategyContainer<AstNodeType> strategies) {
    this(strategies, null, null);
  }

  public ValueCollector setValue(Literal<?> value) {
    return new ValueCollector(strategies, value, variablesRepository);
  }

  public ValueCollector setVariablesRepository(VariablesRepository variablesRepository) {
    return new ValueCollector(strategies, value, variablesRepository);
  }

  public Literal<?> getValue() {
    return value;
  }

  @Override
  public NodeVisitor visit(AstNode node) {
    // recibe identifier -> pide valor al repositorio y se lo queda
    // recibe literal -> guarda valor
    // recibe binExp -> calcula valor bajando por el arbol
    // recibe callExp -> ejecuta metodo y almacena valor
    // vuelve a varDec, assignment, callExp (println()) -> devuelve valor almacenado

    AstNodeType nodeType = node.getNodeType();
    return strategies.getStrategy(nodeType).interpret(node, this);
  }

  @Override
  public OutputResult<?> getOutputResult() {
    return null;
  }
}
