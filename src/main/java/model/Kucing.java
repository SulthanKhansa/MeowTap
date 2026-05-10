package model;

public class Kucing {
    private String idRfid;
    private String nama;
    private String ras;
    private int umur; // bulan
    private String kandang;
    private String statusMakan;
    private String kondisiKesehatan;

    public Kucing() {
    }

    public Kucing(String idRfid, String nama, String ras, int umur, String kandang, String statusMakan, String kondisiKesehatan) {
        this.idRfid = idRfid;
        this.nama = nama;
        this.ras = ras;
        this.umur = umur;
        this.kandang = kandang;
        this.statusMakan = statusMakan;
        this.kondisiKesehatan = kondisiKesehatan;
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

    public String getStatusMakan() {
        return statusMakan;
    }

    public void setStatusMakan(String statusMakan) {
        this.statusMakan = statusMakan;
    }

    public String getKondisiKesehatan() {
        return kondisiKesehatan;
    }

    public void setKondisiKesehatan(String kondisiKesehatan) {
        this.kondisiKesehatan = kondisiKesehatan;
    }
}