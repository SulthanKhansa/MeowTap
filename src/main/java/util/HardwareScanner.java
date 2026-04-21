package util;

import com.fazecast.jSerialComm.SerialPort;
import dao.KucingDAO;
import model.Kucing;

/**
 * Logika komunikasi dengan modul RFID Reader.
 */
public class HardwareScanner {
    
    // Interface untuk mengirim event balik ke UI (MainDashboard)
    public interface ScanCallback {
        void onScanSuccess(Kucing kucing);
        void onScanNotFound(String rawId);
        void onScanError(String message);
    }

    public void mulaiScanning(ScanCallback callback) {
        // Cari port USB yang colok ke alat RFID
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            if (callback != null) callback.onScanError("USB Reader RFID tidak ditemukan.");
            return;
        }

        // Asumsi port pertama adalah RFID reader
        SerialPort rfidPort = ports[0];
        if (!rfidPort.openPort()) {
            if (callback != null) callback.onScanError("Gagal membuka port komunikasi.");
            return;
        }

        rfidPort.setComPortParameters(9600, 8, 1, 0);
        rfidPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        try {
            while (true) {
                byte[] readBuffer = new byte[10];
                int numRead = rfidPort.readBytes(readBuffer, readBuffer.length);
                
                if (numRead > 0) {
                    // ID RFID asli (Plain Text)
                    String idMentah = new String(readBuffer).trim();
                    System.out.println("RFID detected: " + idMentah);
                    
                    // FIX BUG: Cari langsung pakai ID mentah, JANGAN di-hash (karena di DB simpan plain text)
                    KucingDAO dao = new KucingDAO();
                    Kucing kucing = dao.findById(idMentah);
                    
                    if (kucing != null) {
                        if (callback != null) callback.onScanSuccess(kucing);
                    } else {
                        if (callback != null) callback.onScanNotFound(idMentah);
                    }
                }
            }
        } catch (Exception e) {
            if (callback != null) callback.onScanError(e.getMessage());
        } finally {
            rfidPort.closePort();
        }
    }
}