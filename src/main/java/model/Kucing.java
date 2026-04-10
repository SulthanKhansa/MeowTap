package model;

public class Kucing {
    // Enkapsulasi: Semua variabel diset private
    private String idRfid;
    private String nama;
    private String kandang;
    private String statusKesehatan;

    // Constructor kosong (Wajib ada buat framework/database nanti)
    public Kucing() {
    }

    // Constructor dengan parameter buat isi data
    public Kucing(String idRfid, String nama, String kandang, String statusKesehatan) {
        this.idRfid = idRfid;
        this.nama = nama;
        this.kandang = kandang;
        this.statusKesehatan = statusKesehatan;
    }

    // --- GETTER & SETTER (Jalur resmi buat akses variabel private) ---

    public String getIdRfid() {
        return idRfid;
    }

    public void setIdRfid(String idRfid) {
        this.idRfid = idRfid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKandang() {
        return kandang;
    }

    public void setKandang(String kandang) {
        this.kandang = kandang;
    }

    public String getStatusKesehatan() {
        return statusKesehatan;
    }

    public void setStatusKesehatan(String statusKesehatan) {
        this.statusKesehatan = statusKesehatan;
    }
}