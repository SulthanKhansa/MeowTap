package util;

import com.fazecast.jSerialComm.SerialPort;
import dao.KucingDAO;
import model.Kucing;
import java.io.OutputStream;
import util.I18nService;

public class HardwareScanner {
    
    private SerialPort activePort;

    // Callback untuk mengirim hasil scan ke UI
    public interface ScanCallback {
        void onScanSuccess(Kucing kucing);
        void onScanNotFound(String rawId);
        void onScanError(String message);
    }

    public void mulaiScanning(ScanCallback callback) {
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            if (callback != null) callback.onScanError(I18nService.get("scanner.error.notfound"));
            return;
        }

        // Ambil port hardware pertama
        SerialPort rfidPort = ports[0];
        if (!rfidPort.openPort()) {
            if (callback != null) callback.onScanError(I18nService.get("scanner.error.openfailed"));
            return;
        }

        this.activePort = rfidPort;
        rfidPort.setComPortParameters(9600, 8, 1, 0);
        rfidPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        try {
            while (true) {
                byte[] readBuffer = new byte[10];
                int numRead = rfidPort.readBytes(readBuffer, readBuffer.length);
                
                if (numRead > 0) {
                    String idMentah = new String(readBuffer).trim();
                    System.out.println("RFID detected: " + idMentah);
                    
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
            this.activePort = null;
        }
    }

    /**
     * Mengirim perintah/byte ke RFID reader.
     * @param command Byte array perintah
     * @return true jika berhasil, false jika gagal
     */
    public boolean sendCommand(byte[] command) {
        if (activePort == null || !activePort.isOpen()) {
            System.err.println("ERROR: Port belum dibuka atau sudah ditutup.");
            return false;
        }
        try {
            OutputStream outputStream = activePort.getOutputStream();
            outputStream.write(command);
            outputStream.flush();
            System.out.println("INFO: Perintah berhasil dikirim (" + command.length + " bytes)");
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: Gagal mengirim perintah - " + e.getMessage());
            return false;
        }
    }

    /**
     * Mengirim string perintah ke RFID reader (encoding UTF-8).
     * @param command String perintah
     * @return true jika berhasil, false jika gagal
     */
    public boolean sendCommand(String command) {
        return sendCommand(command.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Mendapatkan status koneksi port.
     * @return true jika port terbuka
     */
    public boolean isConnected() {
        return activePort != null && activePort.isOpen();
    }
}