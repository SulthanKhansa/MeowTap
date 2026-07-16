package model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import Serial.SerialDataHandler; 
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import util.I18nService;

public class SerialService {
    private static SerialService instance;
    private SerialPort activePort;
    private final List<SerialDataHandler<String>> handlers = new ArrayList<>();
    private Scanner scanner;
    private InputStream inputStream;
    private OutputStream outputStream;

    // Private constructor untuk Singleton
    private SerialService() {}

    public static synchronized SerialService getInstance() {
        if (instance == null) {
            instance = new SerialService();
        }
        return instance;
    }

    public void addHandler(SerialDataHandler<String> handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeHandler(SerialDataHandler<String> handler) {
        handlers.remove(handler);
    }

    /**
     * Membuka koneksi ke port serial USB RFID Reader.
     */
    public boolean connect(String portName, int baudRate) {
    if (activePort != null && activePort.isOpen()) {
        return true;
    }

    try {
        activePort = SerialPort.getCommPort(portName);
        activePort.setBaudRate(baudRate);
        activePort.setComPortTimeouts(
            SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
            1000,
            0
        );

        if (activePort.openPort()) {

            System.out.println("INFO: Port " + portName + " berhasil dibuka.");

            JOptionPane.showMessageDialog(
                null,
                I18nService.get("serial.connected"),
                I18nService.get("serial.connected.title"),
                JOptionPane.INFORMATION_MESSAGE
            );

            inputStream = activePort.getInputStream();
            scanner = new Scanner(inputStream);
            outputStream = activePort.getOutputStream();

            setupListener();

            // SIMULASI RFID
            SwingUtilities.invokeLater(() -> {
                String data = "1";

                JOptionPane.showMessageDialog(
                    null,
                    I18nService.get("serial.card.detected") + data,
                    I18nService.get("serial.card.title"),
                    JOptionPane.INFORMATION_MESSAGE
                );

                broadcast(data);
            });

            return true;

        } else {

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    I18nService.get("serial.connect.failed") + portName
                        + I18nService.get("serial.connect.failed.hint"),
                    I18nService.get("serial.connect.error"),
                    JOptionPane.ERROR_MESSAGE
                );
            });

            return false;
        }

    } catch (Exception e) {
        System.err.println("ERROR: " + e.getMessage());
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
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;

                try {
                    if (scanner != null && scanner.hasNextLine()) {
                        String data = scanner.nextLine().trim();
                        if (!data.isEmpty()) {
                            
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, 
                                    I18nService.get("serial.card.detected") + data, 
                                    I18nService.get("serial.card.title"), 
                                    JOptionPane.INFORMATION_MESSAGE);
                            });

                            broadcast(data); 
                        }
                    }
                } catch (Exception e) {
                    // Meredam interupsi pembacaan stream biasa
                }
            }
        });
    }

    private void broadcast(String data) {
        List<SerialDataHandler<String>> targets = new ArrayList<>(handlers);
        for (SerialDataHandler<String> handler : targets) {
            handler.onDataReceived(data);
        }
    }

    /**
     * Memutus koneksi port.
     */
    public void disconnect() {
        if (activePort != null && activePort.isOpen()) {
            try {
                activePort.removeDataListener();
                
                if (scanner != null) {
                    scanner.close();
                    scanner = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                
                activePort.closePort();
                System.out.println("INFO: Port serial diputus.");

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        I18nService.get("serial.disconnected"), 
                        I18nService.get("serial.disconnected.title"), 
                        JOptionPane.WARNING_MESSAGE);
                });

            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return activePort != null && activePort.isOpen();
    }

    /**
     * Mengirim data bytes ke port serial.
     * @param data Byte array yang akan dikirim
     * @return true jika berhasil, false jika gagal
     */
    public boolean sendData(byte[] data) {
        if (outputStream == null || activePort == null || !activePort.isOpen()) {
            notifyHandlersSent(false);
            return false;
        }
        try {
            outputStream.write(data);
            outputStream.flush();
            System.out.println("INFO: Data berhasil dikirim (" + data.length + " bytes)");
            notifyHandlersSent(true);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: Gagal mengirim data - " + e.getMessage());
            notifyHandlersSent(false);
            return false;
        }
    }

    /**
     * Mengirim string ke port serial (encoding UTF-8).
     * @param data String yang akan dikirim
     * @return true jika berhasil, false jika gagal
     */
    public boolean sendData(String data) {
        return sendData(data.getBytes(StandardCharsets.UTF_8));
    }

    private void notifyHandlersSent(boolean success) {
        List<SerialDataHandler<String>> targets = new ArrayList<>(handlers);
        for (SerialDataHandler<String> handler : targets) {
            handler.onDataSent(success);
        }
    }
}