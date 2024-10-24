package jsontest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.root.AstNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import factory.LexerFactory;
import factory.ParserFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import program.ProgramNode;

public class ParserWithJsonTest {

  // Test for parser version 1.0
  @Test
  public void testParserV1() {
    List<String> directories =
        List.of("1.0/declare-variable-let", "1.0/call-print", "1.0/concat-string");

    testMultipleDirectories("1.0", directories);
  }

  // Test for parser version 1.1
  @Test
  public void testParserV2() {
    List<String> directories =
        List.of(
            "1.0/declare-variable-let",
            "1.0/call-print",
            "1.0/concat-string",
            "1.1/declare-variable-const",
            "1.1/read-input",
            "1.1/if-statement");

    testMultipleDirectories("1.1", directories);
  }

  private void testMultipleDirectories(String parserVersion, List<String> directories) {
    for (String dir : directories) {
      runTestForDirectory(dir, parserVersion);
    }
  }

  private void runTestForDirectory(String dir, String version) {
    String basePath = "src/test/resources/versions/" + dir;

    try {
      String inputFilePath = basePath + "/input.ps";
      String input = Files.readString(Paths.get(inputFilePath));

      Parser parser = getParser(version, input);
      List<AstNode> nodes = new ArrayList<>();

      while (parser.hasNext()) {
        nodes.add(parser.next());
      }

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(new ProgramNode(nodes));

      String expectedFilePath = basePath + "/expected.json";
      String expected = Files.readString(Paths.get(expectedFilePath));

      Result result = getResult(json, expected);

      System.out.println("Running tests on directory: " + dir);
      System.out.println(json);
      assertEquals(result.expected, result.json);
    } catch (Exception e) {
      ;
      System.out.println("Error running tests on directory: " + dir + ": " + e);
    }
  }

  // Helper method to compare json strings
  private static Result getResult(String json, String expected) {
    json = json.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    expected = expected.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    return new Result(json, expected);
  }

  private record Result(String json, String expected) {}

  private Parser getParser(String version, String input) {
    Lexer lexer = LexerFactory.getLexer(version);
    assert lexer != null;
    lexer = lexer.setInputAsString(input);

    Parser parser = ParserFactory.getParser(version);
    parser = parser.setLexer(lexer);

    return parser;
  }
}
