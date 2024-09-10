package factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lexer.Lexer;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;
import token.types.TokenType;
import token.validators.DataTypePatternChecker;
import token.validators.IdentifierTypeChecker;
import token.validators.OperandPatternChecker;
import token.validators.SyntaxPatternChecker;
import token.validators.TokenTypeGetter;
import token.validators.literal.BooleanTypePatternChecker;
import token.validators.literal.LiteralTypeTokenChecker;
import token.validators.literal.NumberTypePatternChecker;
import token.validators.literal.StringTypePatternChecker;

public class LexerFactory {
  public static Lexer getLexer(String version) throws IOException {
    return switch (version) {
      case "1.0" -> getLexerV1();
      case "1.1" -> getLexerV2();
      default -> throw new IllegalArgumentException("Invalid version");
    };
  }

  // TODO: move constructors to its own class

  private static Lexer getLexerV2() throws IOException {
    IdentifierTypeChecker identifierTypeChecker =
        new IdentifierTypeChecker(Pattern.compile("^[a-zA-Z_][a-zA-Z\\d_]*$"));

    OperandPatternChecker operandPatternChecker =
        new OperandPatternChecker(
            Map.of(
                "+", TokenDataType.OPERAND,
                "-", TokenDataType.OPERAND,
                "*", TokenDataType.OPERAND,
                "/", TokenDataType.OPERAND));

    Map<String, TokenType> reservedWords =
        new HashMap<>(
            Map.of(
                "let", TokenSyntaxType.LET_DECLARATION,
                "(", TokenSyntaxType.OPEN_PARENTHESIS,
                ")", TokenSyntaxType.CLOSE_PARENTHESIS,
                "{", TokenSyntaxType.OPEN_BRACES,
                "}", TokenSyntaxType.CLOSE_BRACES,
                ";", TokenSyntaxType.SEMICOLON,
                ":", TokenSyntaxType.COLON,
                "=", TokenSyntaxType.ASSIGNATION,
                "if", TokenSyntaxType.IF,
                "else", TokenSyntaxType.ELSE));

    reservedWords.put("const", TokenSyntaxType.CONST_DECLARATION);
    reservedWords.put(",", TokenSyntaxType.COMMA);

    SyntaxPatternChecker syntaxPatternChecker = new SyntaxPatternChecker(reservedWords);

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

    String code = "";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    return new Lexer(inputStream, tokenTypeGetter);
  }

  private static Lexer getLexerV1() throws IOException {
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
                "let",
                TokenSyntaxType.LET_DECLARATION,
                "(",
                TokenSyntaxType.OPEN_PARENTHESIS,
                ")",
                TokenSyntaxType.CLOSE_PARENTHESIS,
                ";",
                TokenSyntaxType.SEMICOLON,
                ":",
                TokenSyntaxType.COLON,
                "=",
                TokenSyntaxType.ASSIGNATION,
                ",",
                TokenSyntaxType.COMMA));

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

    String code = "";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    return new Lexer(inputStream, tokenTypeGetter);
  }
}
