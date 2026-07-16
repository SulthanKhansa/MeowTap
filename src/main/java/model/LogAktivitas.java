package model;

import java.time.LocalDateTime;

public class LogAktivitas {
    // Enkapsulasi
    private int idLog;
    private String idRfidKucing; // Ini relasi ke class Kucing
    private String jenisAktivitas; // Contoh: "Makan Pagi", "Cek Medis"
    private LocalDateTime waktuTap;

    // Constructor kosong
    public LogAktivitas() {
    }

    // Constructor dengan parameter
    public LogAktivitas(int idLog, String idRfidKucing, String jenisAktivitas, LocalDateTime waktuTap) {
        this.idLog = idLog;
        this.idRfidKucing = idRfidKucing;
        this.jenisAktivitas = jenisAktivitas;
        this.waktuTap = waktuTap;
    }

    // --- GETTER & SETTER ---

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public String getIdRfidKucing() {
        return idRfidKucing;
    }

    public void setIdRfidKucing(String idRfidKucing) {
        this.idRfidKucing = idRfidKucing;
    }

    public String getJenisAktivitas() {
        return jenisAktivitas;
    }

    public void setJenisAktivitas(String jenisAktivitas) {
        this.jenisAktivitas = jenisAktivitas;
    }

    public LocalDateTime getWaktuTap() {
        return waktuTap;
    }

    public void setWaktuTap(LocalDateTime waktuTap) {
        this.waktuTap = waktuTap;
    }
}