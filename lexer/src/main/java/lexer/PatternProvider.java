package lexer;

import java.util.regex.Pattern;

public class PatternProvider {
  private final String pattern;

  // Default pattern for testing
  public PatternProvider() {
    this.pattern =
        "\"[^\"]*\""
            + // Text between double quotes
            "|'[^']*'"
            + // Text between single quotes
            "|\\d+(\\.\\d+)?[a-zA-Z_]*"
            + // Numbers with optional decimal part and units (with letters)
            "|\\b[a-zA-Z_][a-zA-Z\\d_]*\\b"
            + // Identifiers and keywords
            "|[=;:]"
            + // Equal, semicolon, and colon
            "|[+\\-*/%]"
            + // Arithmetic operands
            "|[()<>{},]"
            + // Parentheses, angle brackets, curly braces, comma
            "|[.]"
            + "|\\S"; // Any other single character (mismatch), excluding spaces
  }

  public PatternProvider(String pattern) {
    this.pattern = pattern;
  }

  public Pattern getPattern() {
    return Pattern.compile(pattern);
  }
}
