package services;

import com.mongodb.client.model.Filters;
import dao.GenericDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import model.LogAktivitas;

public class LogAktivitasService {

    private final GenericDAO<LogAktivitas> dao;

    public LogAktivitasService() {
        this.dao = new GenericDAO<>("log_aktivitas", LogAktivitas.class);
    }

    public void simpanLog(String idRfidKucing, String jenisAktivitas) {
        LogAktivitas log = new LogAktivitas();
        log.setIdLog(UUID.randomUUID().hashCode());
        log.setIdRfidKucing(idRfidKucing);
        log.setJenisAktivitas(jenisAktivitas);
        log.setWaktuTap(LocalDateTime.now());
        dao.save(log);
        System.out.println("LogAktivitasService: Log tersimpan untuk " + idRfidKucing);
    }

    public List<LogAktivitas> getAllLogs() {
        return dao.findAll();
    }

    public List<LogAktivitas> getLogsByRfid(String idRfid) {
        return dao.findMany(Filters.eq("idRfidKucing", idRfid));
    }
}
