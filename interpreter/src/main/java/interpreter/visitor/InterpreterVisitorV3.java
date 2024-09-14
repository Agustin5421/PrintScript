package interpreter.visitor;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;
import output.OutputResult;

import java.util.Map;

// Only works with VariableDeclaration, AssignmentExpression and CallExpression (only println() method).
public class InterpreterVisitorV3 implements NodeVisitor {
    private final VariablesRepository variablesRepository;
    private final Map<AstNodeType, InterpretingStrategy> strategies;
    private final OutputResult<String> outputResult;

    public InterpreterVisitorV3(VariablesRepository variablesRepository, Map<AstNodeType, InterpretingStrategy> strategies, OutputResult<String> outputResult) {
        this.variablesRepository = variablesRepository;
        this.strategies = strategies;
        this.outputResult = outputResult;
    }

    public VariablesRepository getVariablesRepository() {
        return variablesRepository;
    }

    public InterpreterVisitorV3 cloneVisitor() {
        return new InterpreterVisitorV3(variablesRepository, strategies, outputResult);
    }

    @Override
    public NodeVisitor visit(AstNode node) {
        if (strategies.containsKey(node.getNodeType())) {
            return strategies.get(node.getNodeType()).interpret(node, this);
        }
        throw new IllegalArgumentException(node.getNodeType() + " not working in this version :(");
    }

    @Override
    public OutputResult<?> getOutputResult() {
        return outputResult;
    }
}
