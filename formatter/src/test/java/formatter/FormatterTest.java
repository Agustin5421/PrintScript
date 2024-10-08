package formatter;

public class FormatterTest {
  /*
  private final String jsonOptions =
      """
            {
              "colonRules": {
                "before": true,
                "after": true
              },
              "equalSpaces": true,
              "printLineBreaks": 1
            }
            """;
  private final MainFormatter formatter = FormatterInitializer.init(jsonOptions);
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void varDeclarationTest() {
    Identifier name = new Identifier("myVar", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Hello World!\"", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(name, value);
    Program program = new Program(List.of(variableDeclaration));
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program));
  }

  @Test
  public void assignationTest() {
    Identifier name = new Identifier("myVar", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Goodbye World!\"", defaultPosition, defaultPosition);
    AssignmentExpression reAssignation = new AssignmentExpression(name, value, "=");
    Program program = new Program(List.of(reAssignation));
    String formattedCode = """
                myVar = "Goodbye World!";
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program));
  }

  @Test
  public void varDeclarationWithExpressionTest() {
    Identifier name = new Identifier("myVar", defaultPosition, defaultPosition);
    NumberLiteral first = new NumberLiteral(2, defaultPosition, defaultPosition);
    NumberLiteral second = new NumberLiteral(2, defaultPosition, defaultPosition);
    NumberLiteral third = new NumberLiteral(5, defaultPosition, defaultPosition);
    BinaryExpression expression =
        new BinaryExpression(new BinaryExpression(first, second, "+"), third, "*");
    VariableDeclaration declaration = new VariableDeclaration(name, expression);
    Program program = new Program(List.of(declaration));
    String formattedCode = """
                let myVar : number = 2 + 2 * 5;
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program));
  }

  @Test
  public void printLnTest() {
    Identifier name = new Identifier("println", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Hello World!\"", defaultPosition, defaultPosition);
    CallExpression printLnExpression = new CallExpression(name, List.of(value));
    Program program = new Program(List.of(printLnExpression));
    String formattedCode = """
                println("Hello World!");
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program));
  }

  @Test
  public void completeFormattingTest() {
    Parser parser = ParserFactory.getParser("1.0");
    Lexer lexer = LexerFactory.getLexer("1.0");
    List<Token> tokens =
        lexer.tokenize(
            """
                        let myVar     : number
                          = 2 + 3 * 2;
                        println(myVar     )    ;
                        myVar       ="Hello World!";
                        println(     myVar);
                        """);

    // TODO: Formatter should iterate over parser

    Program program = parser.parse(tokens);
    String formattedCode =
        """
                let myVar : number = 2 + 3 * 2;

                println(myVar);
                myVar = "Hello World!";

                println(myVar);
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));


  }

  @Test
  public void differentFormatTest() {
    final String newJsonOptions =
        """
                {
                  "rules": {
                    "colonRules": {
                      "before": true,
                      "after": false
                    },
                    "equalSpaces": false,
                    "printLineBreaks": 2
                  }
                }
                """;

    Parser parser = ParserFactory.getParser("1.0");
    Lexer lexer = LexerFactory.getLexer("1.0");

    List<Token> tokens =
        lexer.tokenize(
            """
                        let myVar : number = 2 + 3 * 2;
                        println(myVar);
                        myVar = "Hello World!";
                        println(myVar);""");

    // TODO: Formatter should iterate over parser

    Program program = parser.parse(tokens);

    String formattedCode =
        """
                let myVar :number=2 + 3 * 2;


                println(myVar);
                myVar="Hello World!";


                println(myVar);
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, newJsonOptions));

  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(
        JsonSyntaxException.class, () -> FormatterInitializer.init("invalid options json"));
  }

  private static Lexer initLexer() {
    return LexerFactory.getLexer("1.0");
  }
  */

}
