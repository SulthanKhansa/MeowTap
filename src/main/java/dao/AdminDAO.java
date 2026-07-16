package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Admin;
import org.bson.Document;
import util.DatabaseConnection;
import util.SecurityUtils;

/**
 * DAO untuk mengelola data Admin dan proses Login.
 */
public class AdminDAO {
    
    private MongoCollection<Document> collection;

    public AdminDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        collection = database.getCollection("user");
        
        // Auto-seed: Kalau koleksi kosong, buat akun default (admin/admin123)
        if (collection.countDocuments() == 0) {
            Admin defaultAdmin = new Admin(
                "tuko", 
                SecurityUtils.hashSHA256("123"), 
                "Tuko"
            );
            
            Document doc = new Document("username", defaultAdmin.getUsername())
                    .append("password", defaultAdmin.getPassword())
                    .append("namaLengkap", defaultAdmin.getNamaLengkap());
            
            collection.insertOne(doc);
            System.out.println("Default admin account seeded (admin/admin123).");
        }
    }

    /**
     * Memvalidasi login.
     * @return Objek Admin jika sukses, null jika gagal.
     */
    public Admin checkLogin(String username, String plainPassword) {
        String hashedPassword = SecurityUtils.hashSHA256(plainPassword);
        
        Document query = new Document("username", username)
                .append("password", hashedPassword);
        
        Document result = collection.find(query).first();
        
        if (result != null) {
            return new Admin(
                result.getString("username"),
                result.getString("password"),
                result.getString("namaLengkap")
            );
        }
        
        return null;
    }

    /**
     * Mendaftarkan admin baru ke database.
     */
    public boolean register(Admin admin) {
        try {
            Document doc = new Document("username", admin.getUsername())
                    .append("password", admin.getPassword())
                    .append("namaLengkap", admin.getNamaLengkap());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Gagal daftar: " + e.getMessage());
            return false;
        }
    }
}
