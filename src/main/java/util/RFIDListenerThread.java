package util;

import util.HardwareScanner.ScanCallback;

/**
 * Thread yang berjalan di background untuk mendengarkan RFID Reader.
 */
public class RFIDListenerThread extends Thread {
    
    private ScanCallback callback;

    public RFIDListenerThread(ScanCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        System.out.println("RFID Listener background thread started.");
        HardwareScanner scanner = new HardwareScanner();
        
        // Memulai proses scanning dan mengirim callback ke UI nantinya
        scanner.mulaiScanning(callback);
    }
}