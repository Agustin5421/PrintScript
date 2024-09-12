package formatter;

import formatter.factory.FormatterInitializer;

public class FormatterV2Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.1");
  }
  /*
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
     MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

   @Test
   public void testBooleanReAssignation() {
     String formattedCode = """
                 anotherVar = false;
                 """;
     MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

   @Test
   public void testReadInput() {
     String formattedCode = """
             let myVar : string = readInput();
             """;
     MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

   @Test
   public void testReadEnv() {
     String formattedCode = """
             let myVar : string = readEnv("ENV_VAR");
             """;
     MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

   @Test
   public void newCompleteFormattingTest() {
     String code =
         "if (true) { if(a){hola=2;} let name: string = \"Oliver\";} else {a=3; a=5; a=6;a=readInput();if(a){a=readEnv(\"ENV_VAR\");}} const a: number = 5;";
     String formattedCode =
         """
                 if (true) {
                 	if (a) {
                 		hola = 2;
                 	}
                 	let name : string = "Oliver";
                 } else {
                 	a = 3;
                 	a = 5;
                 	a = 6;
                 	a = readInput();
                 	if (a) {
                 		a = readEnv("ENV_VAR");
                 	}
                 }
                 const a : number = 5;
                 """;
     MainFormatter formatter = initFormatter(getJsonOptions(), code);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

   @Test
   public void newDifferentFormatTest() {
     String code =
         "if (true) { if(a){hola=2;} let name: string = \"Oliver\";} else {a=3; a=5; a=6;a=readInput();if(a){a=readEnv(\"ENV_VAR\");if(a){a=false;}}} const a: number = 5;";
     String formattedCode =
         """
                 if (true) {
                 				if (a) {
                 								hola=2;
                 				}
                 				let name: string="Oliver";
                 } else {
                 				a=3;
                 				a=5;
                 				a=6;
                 				a=readInput();
                 				if (a) {
                 								a=readEnv("ENV_VAR");
                 								if (a) {
                 												a=false;
                 								}
                 				}
                 }
                 const a: number=5;
                 """;
     MainFormatter formatter = initFormatter(alternativeOptions(), code);
     Assertions.assertEquals(formattedCode, formatter.formatProgram());
   }

  */
}
