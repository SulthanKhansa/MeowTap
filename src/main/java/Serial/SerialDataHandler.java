package Serial;

public interface SerialDataHandler<T> {
    void onDataReceived(T data);
}