package model;

public class Kucing {
    private String idRfid;
    private String nama;
    private String ras;
    private int umur; // bulan
    private String kandang;
    private String statusKesehatan;

    public Kucing() {
    }

    public Kucing(String idRfid, String nama, String ras, int umur, String kandang, String statusKesehatan) {
        this.idRfid = idRfid;
        this.nama = nama;
        this.ras = ras;
        this.umur = umur;
        this.kandang = kandang;
        this.statusKesehatan = statusKesehatan;
    }

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

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
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