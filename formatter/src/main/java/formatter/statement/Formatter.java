package formatter.statement;

import ast.root.AstNode;
import com.google.gson.JsonObject;

public interface Formatter {
  boolean shouldFormat(AstNode statement);

  String format(AstNode node, JsonObject rules, String currentProgram);
}
