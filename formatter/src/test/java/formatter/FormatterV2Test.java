package formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runner.TestRunner;

public class FormatterV2Test extends AbstractFormatterTest {
  @Override
  protected TestRunner setRunner(String jsonOptions, String formattedCode) {
    return new TestRunner(jsonOptions, formattedCode, "1.1");
  }

  @Override
  public String getJsonOptions() {
    return """
            {
              "colonRules": {
                "before": true,
                "after": true
              },
              "equalSpaces": true,
              "printLineBreaks": 1,
              "indentSize": 1
            }
            """;
  }

  @Override
  public String alternativeOptions() {
    return """
            {
              "colonRules": {
                "before": false,
                "after": true
              },
              "equalSpaces": false,
              "printLineBreaks": 2,
              "indentSize": 4
            }
            """;
  }

  // TODO : Add additional tests for the new formatter
  @Test
  public void testBooleanDeclaration() {
    String formattedCode = """
                let myVar : boolean = true;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testBooleanReAssignation() {
    String formattedCode =
        """
                let anotherVar : boolean = true;
                anotherVar = false;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testReadInput() {
    String formattedCode = """
            let myVar : string = readInput();
            """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testReadEnv() {
    String formattedCode = """
            let myVar : string = readEnv("ENV_VAR");
            """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  // TODO: Fix this test

  @Test
  public void newCompleteFormattingTest() {
    String code =
        "if (true) { if(true){let hola: number=2;} let name: string = \"Oliver\";}"
            + "else {let a: number=3; a=5; a=6;let c: string=readInput(\"Name:\");"
            + "if(false){let d: string=readEnv(\"ENV_VAR\");}} "
            + "const b: number = 5;";
    String formattedCode =
        """
                if (true) {
                	if (true) {
                		let hola : number = 2;
                	}
                	let name : string = "Oliver";
                } else {
                	let a : number = 3;
                	a = 5;
                	a = 6;
                	let c : string = readInput("Name:");
                	if (false) {
                		let d : string = readEnv("ENV_VAR");
                	}
                }
                const b : number = 5;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), code);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  // TODO: fix this test

  @Test
  public void newDifferentFormatTest() {
    String code =
        "let hola : string = 'hola'; "
            + "if (true) { if(false){hola='chau';} let name: string = \"Oliver\";} "
            + "const a: number = 5;";
    String formattedCode =
        """
                let hola: string="hola";
                if (true) {
                				if (false) {
                								hola="chau";
                				}
                				let name: string="Oliver";
                }
                const a: number=5;
                """;
    TestRunner testRunner = setRunner(alternativeOptions(), code);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }
}
