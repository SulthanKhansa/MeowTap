package util;

public class RFIDListenerThread extends Thread {
    
    public RFIDListenerThread() {
        // Ini kuncinya: setDaemon(true)
        // Artinya thread ini cuma "pembantu". Kalo aplikasi utama di-close, thread ini bakal otomatis ikut mati
        setDaemon(true);
    }

    @Override
    public void run() {
        System.out.println("Background Thread: Scanner RFID udah standby nunggu tap wkwk");
        
        while (true) {
            try {
                // Di sini nanti lu masukin kodingan buat ngebaca input dari USB Serial (Pertemuan 13)
                // Sementara kita kasih jeda 3 detik aja buat simulasi
                Thread.sleep(3000); 
                
            } catch (InterruptedException e) {
                System.out.println("Background Thread dimatiin paksa.");
                break;
            }
        }
    }
}