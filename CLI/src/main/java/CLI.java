
import ast.root.Program;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import interpreter.Interpreter;
import lexer.Lexer;
import parsers.Parser;
import token.Token;
import token.tokenTypeCheckers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



public class CLI {
    private static final int TOTAL_MODULES = 3;
    private static final int PERCENTAGE_PER_MODULE = 100 / TOTAL_MODULES;

    public void executeFile(String filePath) {
        File file = new File(filePath);

        validateFile(filePath, file);

        String content = getCode(filePath);

        ProgressPrinter progressPrinter = new ProgressPrinter();
        ProgressObserver observer = new ProgressObserver(progressPrinter);

        Lexer lexer = initLexer(observer);
        Parser parser = new Parser(observer);
        Interpreter interpreter = new Interpreter(observer);

        registerModules(observer);

        executeProgram(lexer, content, observer, parser, interpreter);
        System.out.println();
    }

    private static void executeProgram(Lexer lexer, String content, ProgressObserver observer, Parser parser, Interpreter interpreter) {
        try {
            List<Token> tokens = lexer.extractTokens(content);
            observer.update("lexer", 100);

            Program program = parser.parse(tokens);
            observer.update("parser", 100);

            interpreter.executeProgram(program);
            observer.update("interpreter", 100);

            observer.finish();
        } catch (Exception e) {
            observer.error(100);
            System.out.println();
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        /* if (args.length == 0) {
            System.out.println("Please enter a file.");
            return;
        } */

        String filePath =  "CLI/clitest.txt"; //args[0];
        CLI fileExecutor = new CLI();
        fileExecutor.executeFile(filePath);
    }

    private static void registerModules(ProgressObserver observer) {
        observer.registerModule("lexer", PERCENTAGE_PER_MODULE);
        observer.registerModule("parser", PERCENTAGE_PER_MODULE);
        observer.registerModule("interpreter", PERCENTAGE_PER_MODULE);
    }

    private static void validateFile(String filePath, File file) {
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);

        }
    }

    private static String getCode(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred reading the file: " + e.getMessage());
        }
        return content.toString();
    }

    private static Lexer initLexer(ProgressObserver observer) {
        TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
        OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
        DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
        IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

        TokenTypeChecker tokenTypeChecker = new TokenTypeChecker(List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

        return new Lexer(tokenTypeChecker, observer);
    }
}