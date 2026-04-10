package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    // Alamat MongoDB lokal di laptop lu
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    // Nama database lu (otomatis dibikinin sama Mongo kalo belom ada)
    private static final String DATABASE_NAME = "meowtap_db";
    private static MongoClient mongoClient = null;

    // Fungsi handshake buat dipanggil di class lain
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                // Proses buka gerbang koneksi
                mongoClient = MongoClients.create(CONNECTION_URI);
                System.out.println("Mantap, koneksi ke MongoDB MeowTap berhasil wkwk");
            } catch (Exception e) {
                System.out.println("Yah error konek MongoDB: " + e.getMessage());
            }
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}