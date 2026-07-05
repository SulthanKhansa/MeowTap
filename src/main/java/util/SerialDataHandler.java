package util;

/**
 * Interface callback untuk menerima data dari Serial Port (Observer Pattern).
 * @param <T> Tipe data yang diterima
 */
public interface SerialDataHandler<T> {
    void onDataReceived(T data);
}
