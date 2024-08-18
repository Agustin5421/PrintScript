package formatter.statement;

import ast.root.ASTNode;
import com.google.gson.JsonObject;

public interface Formatter {
    boolean shouldFormat(ASTNode statement);
    String format(ASTNode node, JsonObject rules, String currentProgram);
}
