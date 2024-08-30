package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternHandler {
  private static final String TEXT_PATTERNS =
      "\"[^\"]*\""
          + "|'[^']*'"
          + "|\\d+(\\.\\d+)?[a-zA-Z_]*"
          + "|\\b[a-zA-Z_][a-zA-Z\\d_]*\\b"
          + "|[=;:]"
          + "|[+\\-*/%]"
          + "|[()<>{},]"
          + "|[.]"
          + "|\\S";

  private final Pattern pattern;

  public PatternHandler() {
    this.pattern = Pattern.compile(TEXT_PATTERNS);
  }

  public List<String> extractWords(String code) {
    List<String> words = new ArrayList<>();
    Matcher matcher = pattern.matcher(code);

    while (matcher.find()) {
      words.add(matcher.group());
    }

    return words;
  }

  public List<int[]> extractPositions(String code) {
    List<int[]> positions = new ArrayList<>();
    Matcher matcher = pattern.matcher(code);

    while (matcher.find()) {
      positions.add(new int[] {matcher.start(), matcher.end()});
    }

    return positions;
  }
}
