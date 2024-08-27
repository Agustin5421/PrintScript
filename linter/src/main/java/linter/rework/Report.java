package linter.rework;

import token.Position;

public record Report(Position initialPosition, Position finalPosition, String message) {
  @Override
  public String toString() {
    return "Warning from " + initialPosition + " to " + finalPosition + ": " + message;
  }
}
