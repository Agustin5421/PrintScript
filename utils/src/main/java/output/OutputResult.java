package output;

public interface OutputResult<T> {
  void saveResult(T result);

  T getResult();
}
