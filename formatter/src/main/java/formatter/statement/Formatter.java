package formatter.statement;

import ast.root.AstNode;
import com.google.gson.JsonObject;

public interface Formatter {
  String format(AstNode node, JsonObject rules, String currentProgram);
}
