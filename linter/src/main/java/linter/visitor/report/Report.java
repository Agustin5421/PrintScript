package linter.visitor.report;

import token.Position;

public record Report(Position start, Position end, String message) {
  @Override
  public String toString() {
    return "Warning from " + start + " to " + end + ": " + message;
  }
}
