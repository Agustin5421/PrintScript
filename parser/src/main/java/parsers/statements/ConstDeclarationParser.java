package parsers.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.statements.ConstDeclaration;
import ast.statements.Statement;
import java.util.List;
// TODO: Declarations should be an interface
// TODO: Test
import parsers.Parser;
import token.Token;
import token.types.TokenSyntaxType;

public class ConstDeclarationParser implements StatementParser {
  @Override
  public Statement parse(Parser parser, List<Token> tokens) {
    Identifier name =
        new Identifier(
            tokens.get(1).value(), tokens.get(1).initialPosition(), tokens.get(1).finalPosition());
    Expression value = parser.parseExpression(tokens.subList(4, tokens.size() - 1));

    return new ConstDeclaration(name, value, name.start(), value.end());
  }

  @Override
  public boolean shouldParse(List<Token> tokens) {
    return tokens.get(0).nodeType().equals(TokenSyntaxType.CONST_DECLARATION);
  }
}
