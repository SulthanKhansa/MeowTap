package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    // Konfigurasi koneksi MongoDB lokal
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "meowtap_db";
    private static MongoClient mongoClient = null;

    /**
     * Mendapatkan koneksi ke database MongoDB.
     * @return MongoDatabase instance
     */
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_URI);
                System.out.println("Berhasil terhubung ke database: " + DATABASE_NAME);
            } catch (Exception e) {
                System.err.println("Gagal terhubung ke MongoDB: " + e.getMessage());
            }
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}