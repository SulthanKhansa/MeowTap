package util;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Service untuk komunikasi Serial Port (2-Way: Read & Write).
 * Menggunakan Singleton Pattern dan Observer Pattern untuk notifikasi data masuk.
 */
public class SerialService {

    private static SerialService instance;
    private SerialPort activePort;
    private final List<SerialDataHandler<String>> handlers = new ArrayList<>();

    private SerialService() {
    }

    public static synchronized SerialService getInstance() {
        if (instance == null) {
            instance = new SerialService();
        }
        return instance;
    }

    /**
     * Menambahkan handler observer untuk menerima notifikasi data masuk.
     */
    public void addHandler(SerialDataHandler<String> handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    /**
     * Menghapus handler observer (mencegah memory leak).
     */
    public void removeHandler(SerialDataHandler<String> handler) {
        handlers.remove(handler);
    }

    /**
     * Membuka koneksi ke port serial.
     * @param portName Nama port (misal "COM3")
     * @param baudRate Baud rate (misal 9600)
     * @return true jika berhasil
     */
    public boolean connect(String portName, int baudRate) {
        if (activePort != null && activePort.isOpen()) {
            return true;
        }

        activePort = SerialPort.getCommPort(portName);
        activePort.setBaudRate(baudRate);
        activePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

        if (activePort.openPort()) {
            System.out.println("INFO: Port " + portName + " terbuka.");
            setupListener();
            return true;
        } else {
            System.err.println("ERROR: Gagal membuka port " + portName);
            return false;
        }
    }

    /**
     * Mengatur listener event untuk mendeteksi data masuk secara otomatis.
     */
    private void setupListener() {
        activePort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }

                try (Scanner scanner = new Scanner(activePort.getInputStream())) {
                    if (scanner.hasNextLine()) {
                        String data = scanner.nextLine().trim();
                        if (!data.isEmpty()) {
                            broadcast(data);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error membaca data serial: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Mengirimkan data ke semua handler yang terdaftar.
     */
    public void broadcast(String data) {
        for (SerialDataHandler<String> handler : handlers) {
            handler.onDataReceived(data);
        }
    }

    /**
     * Mengirim data balik ke serial port (2-Way Communication).
     * @param data Data yang akan dikirim
     * @return true jika berhasil dikirim
     */
    public boolean sendData(String data) {
        if (activePort != null && activePort.isOpen()) {
            byte[] dataBytes = data.getBytes();
            int bytesWritten = activePort.writeBytes(dataBytes, dataBytes.length);
            return bytesWritten > 0;
        }
        return false;
    }

    /**
     * Mengirim array byte langsung ke serial port.
     * @param dataBytes Array byte yang akan dikirim
     * @return Jumlah byte yang berhasil dikirim
     */
    public int sendBytes(byte[] dataBytes) {
        if (activePort != null && activePort.isOpen()) {
            return activePort.writeBytes(dataBytes, dataBytes.length);
        }
        return 0;
    }

    /**
     * Menutup koneksi serial port.
     */
    public void disconnect() {
        if (activePort != null && activePort.isOpen()) {
            activePort.removeDataListener();
            activePort.closePort();
            System.out.println("INFO: Port ditutup.");
        }
    }

    /**
     * Mengecek apakah port sedang terbuka.
     */
    public boolean isConnected() {
        return activePort != null && activePort.isOpen();
    }

    /**
     * Mendapatkan semua port yang tersedia.
     */
    public static String[] getAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName();
        }
        return portNames;
    }

    /**
     * Simulasi broadcast data (untuk testing tanpa hardware).
     */
    public void simulateBroadcast(String dummyData) {
        broadcast(dummyData);
    }
}
