package model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import Serial.SerialDataHandler; 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SerialService {
    private static SerialService instance;
    private SerialPort activePort;
    private final List<SerialDataHandler<String>> handlers = new ArrayList<>();
    private Scanner scanner;
    private InputStream inputStream;

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
            activePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

            if (activePort.openPort()) {
                System.out.println("INFO: Port " + portName + " berhasil dibuka.");
                
                inputStream = activePort.getInputStream();
                scanner = new Scanner(inputStream);
                
                setupListener();

                // POPUP: Notifikasi berhasil koneksi ke alat
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        "Alat RFID Reader berhasil terhubung di " + portName, 
                        "Koneksi Berhasil", 
                        JOptionPane.INFORMATION_MESSAGE);
                });

                return true;
            } else {
                // POPUP: Notifikasi gagal membuka port
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        "Gagal membuka port " + portName + ". Pastikan alat sudah ditancapkan!", 
                        "Koneksi Gagal", 
                        JOptionPane.ERROR_MESSAGE);
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
                            
                            // POPUP info ketika kartu terdeteksi
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, 
                                    "Kartu RFID Terdeteksi!\nID: " + data, 
                                    "MeowTap Scanner", 
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
                
                activePort.closePort();
                System.out.println("INFO: Port serial diputus.");

                // POPUP: Notifikasi koneksi diputus
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, 
                        "Koneksi ke RFID Reader telah diputus.", 
                        "Koneksi Diputus", 
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
}