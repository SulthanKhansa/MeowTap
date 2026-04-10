package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    
    // Fungsi buat ngacak teks biasa jadi kode rahasia (Hash SHA-256)
    public static String hashSHA256(String input) {
        try {
            // Manggil mesin pembuat hash bawaan Java
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            
            // Ubah hasil acakan byte jadi format teks hexadesimal biar gampang dibaca database
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            // Balikin hasil teks yang udah diacak
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Yah algoritma hashingnya error: " + e.getMessage());
            return null;
        }
    }
}