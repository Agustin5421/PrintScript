package linter.rework.report;

import token.Position;

public record Report(Position initialPosition, Position finalPosition, String message) {
  @Override
  public String toString() {
    return "Warning from " + initialPosition + " to " + finalPosition + ": " + message;
  }
}
