package report;

import position.Position;

public record Report(Position start, Position end, String message) {
  @Override
  public String toString() {
    return "Warning from " + start + " to " + end + ": " + message;
  }
}
