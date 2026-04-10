package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import model.Kucing;
import org.bson.Document;
import util.DatabaseConnection;

// KucingDAO ini "mengontrak" kerangka DataAccessObject yang kemaren kita bikin
public class KucingDAO implements DataAccessObject<Kucing> {

    private MongoCollection<Document> collection;

    public KucingDAO() {
        // Manggil koneksi yang udah lu bikin di pertemuan 5
        MongoDatabase database = DatabaseConnection.getDatabase();
        // Bikin atau milih tabel (kalo di Mongo namanya collection) "kucing"
        collection = database.getCollection("kucing");
    }

    // FUNGSI CREATE (Simpan Data)
    @Override
    public void insert(Kucing data) {
        // Ubah objek Java lu jadi format dokumen BSON/JSON nya MongoDB
        Document doc = new Document("idRfid", data.getIdRfid())
                .append("nama", data.getNama())
                .append("kandang", data.getKandang())
                .append("statusKesehatan", data.getStatusKesehatan());
        
        // Simpen ke database
        collection.insertOne(doc);
        System.out.println("Sip, data si " + data.getNama() + " berhasil masuk database wkwk");
    }

    // FUNGSI READ (Tampil Semua Data)
    @Override
    public List<Kucing> findAll() {
        List<Kucing> listKucing = new ArrayList<>();
        
        // Tarik semua data dari collection "kucing"
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                
                // Terjemahin balik dari JSON MongoDB ke objek Java
                Kucing k = new Kucing(
                        doc.getString("idRfid"),
                        doc.getString("nama"),
                        doc.getString("kandang"),
                        doc.getString("statusKesehatan")
                );
                listKucing.add(k);
            }
        }
        return listKucing;
    }

    // FUNGSI UPDATE (Edit Data)
    @Override
    public void update(Kucing data) {
        // Panggil helper buat filter dan update dari MongoDB
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", data.getIdRfid());
        
        // Kita set data apa aja yang mau diubah
        org.bson.conversions.Bson updateOperation = com.mongodb.client.model.Updates.combine(
            com.mongodb.client.model.Updates.set("nama", data.getNama()),
            com.mongodb.client.model.Updates.set("kandang", data.getKandang()),
            com.mongodb.client.model.Updates.set("statusKesehatan", data.getStatusKesehatan())
        );
        
        // Eksekusi update ke database
        collection.updateOne(filter, updateOperation);
        System.out.println("Sip, data si " + data.getNama() + " udah berhasil di-update wkwk");
    }

    // FUNGSI DELETE (Hapus Data)
    @Override
    public void delete(String id) {
        // Cari berdasarkan idRfid terus hapus
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", id);
        collection.deleteOne(filter);
        System.out.println("Data kucing dengan ID " + id + " udah dihapus dari muka bumi wkwk");
    }

    // FUNGSI FINDBYID (Cari Satu Data Spesifik buat ditampilin ke form edit)
    @Override
    public Kucing findById(String id) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", id);
        Document doc = collection.find(filter).first();
        
        // Kalo kucingnya ketemu di database, kita ubah jadi objek Kucing
        if (doc != null) {
            return new Kucing(
                doc.getString("idRfid"),
                doc.getString("nama"),
                doc.getString("kandang"),
                doc.getString("statusKesehatan")
            );
        }
        // Kalo ga ketemu
        return null; 
        }
    
    }