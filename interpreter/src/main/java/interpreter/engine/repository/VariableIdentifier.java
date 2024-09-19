package interpreter.engine.repository;

public record VariableIdentifier(String name, String type, boolean isModifiable) {
  public VariableIdentifier(String name) {
    this(name, "unknown", true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    VariableIdentifier that = (VariableIdentifier) obj;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
