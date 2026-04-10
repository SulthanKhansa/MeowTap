package util;

import javax.swing.JLabel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.SwingUtilities;

// Pake "extends Thread" buat nandain kalo class ini bakal jalan paralel
public class ClockThread extends Thread {
    private JLabel timeLabel;
    private boolean isRunning = true;

    // Konstruktor ini bakal nerima komponen JLabel dari UI buatan temen lu nanti
    public ClockThread(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        while (isRunning) {
            String currentTime = LocalDateTime.now().format(formatter);
            
            // SwingUtilities.invokeLater ini wajib dipake kalo mau ngubah UI dari dalam thread
            // Biar ga tabrakan dan ga bikin Java Swing lu crash wkwk
            SwingUtilities.invokeLater(() -> {
                timeLabel.setText("Waktu Saat Ini: " + currentTime);
            });
            
            try {
                // Suruh thread-nya tidur 1 detik biar detiknya pas
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                System.out.println("Thread jam berhenti wkwk");
            }
        }
    }
    
    // Fungsi buat matiin jamnya misal admin udah logout
    public void stopClock() {
        isRunning = false;
    }
}