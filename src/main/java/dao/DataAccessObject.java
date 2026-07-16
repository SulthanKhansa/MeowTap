package dao;

import java.util.List;

// Huruf <T> ini adalah Generic Type
public interface DataAccessObject<T> {
    
    // Fungsi buat nambah data baru ke database
    void insert(T data);
    
    // Fungsi buat ngedit data yang udah ada
    void update(T data);
    
    // Fungsi buat ngapus data 
    void delete(String id);
    
    // Fungsi buat nyari satu data spesifik berdasarkan ID
    T findById(String id);
    
    // Fungsi buat narik semua data dari tabel
    List<T> findAll();
    
}