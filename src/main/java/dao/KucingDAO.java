package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import model.Kucing;
import org.bson.Document;
import util.DatabaseConnection;

// DAO untuk mengelola data Kucing di MongoDB.
 
public class KucingDAO implements DataAccessObject<Kucing> {

    private MongoCollection<Document> collection;

    public KucingDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        collection = database.getCollection("kucing");
    }

    @Override
    public void insert(Kucing data) {
        Document doc = new Document("idRfid", data.getIdRfid())
                .append("nama", data.getNama())
                .append("ras", data.getRas())
                .append("umur", data.getUmur())
                .append("kandang", data.getKandang())
                .append("statusMakan", data.getStatusMakan())
                .append("kondisiKesehatan", data.getKondisiKesehatan());
        
        collection.insertOne(doc);
        System.out.println("Data " + data.getNama() + " berhasil ditambahkan.");
    }

    @Override
    public List<Kucing> findAll() {
        List<Kucing> listKucing = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Kucing k = new Kucing(
                        doc.getString("idRfid"),
                        doc.getString("nama"),
                        doc.getString("ras"),
                        doc.getInteger("umur", 0),
                        doc.getString("kandang"),
                        doc.getString("statusMakan"),
                        doc.getString("kondisiKesehatan")
                );
                listKucing.add(k);
            }
        }
        return listKucing;
    }

    @Override
    public void update(Kucing data) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", data.getIdRfid());
        org.bson.conversions.Bson updateOperation = com.mongodb.client.model.Updates.combine(
            com.mongodb.client.model.Updates.set("nama", data.getNama()),
            com.mongodb.client.model.Updates.set("ras", data.getRas()),
            com.mongodb.client.model.Updates.set("umur", data.getUmur()),
            com.mongodb.client.model.Updates.set("kandang", data.getKandang()),
            com.mongodb.client.model.Updates.set("statusMakan", data.getStatusMakan()),
            com.mongodb.client.model.Updates.set("kondisiKesehatan", data.getKondisiKesehatan())
        );
        collection.updateOne(filter, updateOperation);
    }

    @Override
    public void delete(String id) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", id);
        collection.deleteOne(filter);
    }

    @Override
    public Kucing findById(String id) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.eq("idRfid", id);
        Document doc = collection.find(filter).first();
        if (doc != null) {
            return new Kucing(
                doc.getString("idRfid"),
                doc.getString("nama"),
                doc.getString("ras"),
                doc.getInteger("umur", 0),
                doc.getString("kandang"),
                doc.getString("statusMakan"),
                doc.getString("kondisiKesehatan")
            );
        }
        return null; 
    }

    // Menghitung jumlah kucing berdasarkan status makan (Case-Insensitive).
    public long countByStatus(String status) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.regex("statusMakan", "^" + status + "$", "i");
        return collection.countDocuments(filter);
    }

    // Menghitung jumlah kucing berdasarkan kondisi kesehatan tertentu (Case-Insensitive).
    public long countByKondisi(String kondisi) {
        org.bson.conversions.Bson filter = com.mongodb.client.model.Filters.regex("kondisiKesehatan", "^" + kondisi + "$", "i");
        return collection.countDocuments(filter);
    }
}