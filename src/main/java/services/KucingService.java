package services;

import dao.GenericDAO;
import java.util.List;
import model.Kucing;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;

public class KucingService {

    private final GenericDAO<Kucing> dao;

    public KucingService() {
        this.dao = new GenericDAO<>("kucing", Kucing.class);
    }

    public void tambahKucing(Kucing kucing) {
        dao.save(kucing);
        System.out.println("KucingService: " + kucing.getNama() + " ditambahkan.");
    }

    public List<Kucing> tampilSemua() {
        return dao.findAll();
    }

    public Kucing cariById(String idRfid) {
        return dao.findOne(Filters.eq("idRfid", idRfid));
    }

    public List<Kucing> cariByNama(String nama) {
        return dao.findMany(Filters.regex("nama", nama, "i"));
    }

    public void updateKucing(Kucing kucing) {
        dao.update(Filters.eq("idRfid", kucing.getIdRfid()), kucing);
    }

    public void hapusKucing(String idRfid) {
        dao.delete(Filters.eq("idRfid", idRfid));
    }

    public long countByStatus(String status) {
        return dao.findMany(Filters.eq("statusKesehatan", status)).size();
    }

    public long countByKondisi(String kondisi) {
        return dao.findMany(Filters.eq("kondisiKesehatan", kondisi)).size();
    }

    public long countByStatusMakan(String statusMakan) {
        return dao.findMany(Filters.eq("statusMakan", statusMakan)).size();
    }
}
