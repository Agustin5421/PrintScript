import lexer.Lexer;
import parsers.*;
import parsers.expressions.BinaryExpressionParser;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionParser;
import parsers.statements.VariableDeclarationParser;
import token.tokenTypeCheckers.*;

import java.util.List;

public class ContextProvider {


    private static Parser initFullParser() {
        VariableDeclarationParser variableDeclarationParser = new VariableDeclarationParser();
        AssignmentParser assignmentExpressionParser = new AssignmentParser();
        CallFunctionParser callExpressionParser = new CallFunctionParser();

        return new Parser();
    }


    static Parser initBinaryExpressionParser() {
        VariableDeclarationParser variableDeclarationParser = new VariableDeclarationParser();
        BinaryExpressionParser binaryExpressionParser = new BinaryExpressionParser();

        return new Parser();
    }

    static Lexer initLexer() {
        TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
        OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
        DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
        IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

        TokenTypeChecker tokenTypeChecker = new TokenTypeChecker(List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

        return new Lexer(tokenTypeChecker);
    }
}
