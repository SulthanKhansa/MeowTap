package util;

import com.fazecast.jSerialComm.SerialPort;
import dao.KucingDAO;
import model.Kucing;

public class HardwareScanner {
    
    // Fungsi ini dipanggil dari background thread yang lu bikin di Pertemuan 12
    public void mulaiScanning() {
        // Cari port USB yang colok ke alat RFID
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            System.out.println("Yah, USB Reader RFID belom dicolok wkwk");
            return;
        }

        // Asumsiin aja port pertama itu alat RFID kita
        SerialPort rfidPort = ports[0];
        if (rfidPort.openPort()) {
            System.out.println("Berhasil nyambung ke RFID Reader di " + rfidPort.getSystemPortName());
        } else {
            System.out.println("Gagal buka port USB.");
            return;
        }

        rfidPort.setComPortParameters(9600, 8, 1, 0); // Settingan standar pabrik RFID
        rfidPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        try {
            while (true) {
                byte[] readBuffer = new byte[10]; // Asumsi ID RFID panjangnya 10 karakter
                int numRead = rfidPort.readBytes(readBuffer, readBuffer.length);
                
                if (numRead > 0) {
                    // Dapet nih ID mentahnya dari alat
                    String idMentah = new String(readBuffer).trim();
                    System.out.println("Bunyi BEEP, dapet ID: " + idMentah);
                    
                    // Pertemuan 9: Enkripsi ID-nya biar aman
                    String idAman = SecurityUtils.hashSHA256(idMentah);
                    
                    // Pertemuan 6: Cek ke MongoDB lewat DAO
                    KucingDAO dao = new KucingDAO();
                    Kucing siOyen = dao.findById(idAman); // Pake ID yang udah di-hash
                    
                    if (siOyen != null) {
                        System.out.println("Wah ada " + siOyen.getNama() + " dari kandang " + siOyen.getKandang() + " wkwk");
                        // Di sini nanti lu panggil kodingan buat nampilin fotonya ke UI lu
                    } else {
                        System.out.println("Loh, kucing ini belom terdaftar di database kita.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Scanner mati: " + e.getMessage());
        } finally {
            rfidPort.closePort();
        }
    }
}