package factory;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lexer.Lexer;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;
import token.validators.*;
import token.validators.literal.BooleanTypePatternChecker;
import token.validators.literal.LiteralTypeTokenChecker;
import token.validators.literal.NumberTypePatternChecker;
import token.validators.literal.StringTypePatternChecker;

public class LexerFactory {
  public static Lexer getLexer(String version) {
    return switch (version) {
      case "1.0" -> getLexerV1();
      case "1.1" -> getLexerV2();
      default -> throw new IllegalArgumentException("Invalid version");
    };
  }

  // TODO: move constructors to its own class

  private static Lexer getLexerV2() {
    IdentifierTypeChecker identifierTypeChecker =
        new IdentifierTypeChecker(Pattern.compile("^[a-zA-Z_][a-zA-Z\\d_]*$"));

    OperandPatternChecker operandPatternChecker =
        new OperandPatternChecker(
            Map.of(
                "+", TokenDataType.OPERAND,
                "-", TokenDataType.OPERAND,
                "*", TokenDataType.OPERAND,
                "/", TokenDataType.OPERAND));

    SyntaxPatternChecker syntaxPatternChecker =
        new SyntaxPatternChecker(
            Map.of(
                "let", TokenSyntaxType.DECLARATION,
                "(", TokenSyntaxType.OPEN_PARENTHESIS,
                ")", TokenSyntaxType.CLOSE_PARENTHESIS,
                "{", TokenSyntaxType.OPEN_BRACES,
                "}", TokenSyntaxType.CLOSE_BRACES,
                ";", TokenSyntaxType.SEMICOLON,
                ":", TokenSyntaxType.COLON,
                "=", TokenSyntaxType.ASSIGNATION,
                "if", TokenSyntaxType.IF,
                "else", TokenSyntaxType.ELSE));

    DataTypePatternChecker dataTypePatternChecker =
        new DataTypePatternChecker(
            Map.of(
                "string", TokenDataType.STRING_TYPE,
                "number", TokenDataType.NUMBER_TYPE,
                "boolean", TokenDataType.BOOLEAN_TYPE));

    LiteralTypeTokenChecker literalTypeTokenChecker =
        new LiteralTypeTokenChecker(
            List.of(
                new NumberTypePatternChecker(),
                new StringTypePatternChecker(),
                new BooleanTypePatternChecker()));

    TokenTypeGetter tokenTypeGetter =
        new TokenTypeGetter(
            List.of(
                operandPatternChecker,
                syntaxPatternChecker,
                dataTypePatternChecker,
                literalTypeTokenChecker,
                identifierTypeChecker));

    return new Lexer(tokenTypeGetter);
  }

  private static Lexer getLexerV1() {
    IdentifierTypeChecker identifierTypeChecker =
        new IdentifierTypeChecker(Pattern.compile("^[a-zA-Z_][a-zA-Z\\d_]*$"));

    OperandPatternChecker operandPatternChecker =
        new OperandPatternChecker(
            Map.of(
                "+", TokenDataType.OPERAND,
                "-", TokenDataType.OPERAND,
                "*", TokenDataType.OPERAND,
                "/", TokenDataType.OPERAND));

    SyntaxPatternChecker syntaxPatternChecker =
        new SyntaxPatternChecker(
            Map.of(
                "let", TokenSyntaxType.DECLARATION,
                "(", TokenSyntaxType.OPEN_PARENTHESIS,
                ")", TokenSyntaxType.CLOSE_PARENTHESIS,
                "{", TokenSyntaxType.OPEN_BRACES,
                "}", TokenSyntaxType.CLOSE_BRACES,
                ";", TokenSyntaxType.SEMICOLON,
                ":", TokenSyntaxType.COLON,
                "=", TokenSyntaxType.ASSIGNATION));

    DataTypePatternChecker dataTypePatternChecker =
        new DataTypePatternChecker(
            Map.of(
                "string", TokenDataType.STRING_TYPE,
                "number", TokenDataType.NUMBER_TYPE));

    LiteralTypeTokenChecker literalTypeTokenChecker =
        new LiteralTypeTokenChecker(
            List.of(new NumberTypePatternChecker(), new StringTypePatternChecker()));

    TokenTypeGetter tokenTypeGetter =
        new TokenTypeGetter(
            List.of(
                operandPatternChecker,
                syntaxPatternChecker,
                dataTypePatternChecker,
                literalTypeTokenChecker,
                identifierTypeChecker));
    return new Lexer(tokenTypeGetter);
  }
}
