package util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility untuk enkripsi dan dekripsi data menggunakan algoritma AES-128.
 * Digunakan untuk mengamankan data sensitif seperti ID Karyawan.
 */
public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String KEY = System.getProperty("KEY");
    private static final byte[] SECRET_KEY = (KEY != null) ? KEY.getBytes() : "DefaultKey12345".getBytes();

    /**
     * Mengubah teks biasa menjadi teks tersandi (Enkripsi).
     * @param value Teks mentah
     * @return Teks tersandi dalam format Base64
     */
    public static String encrypt(String value) {
        try {
            SecretKeySpec spec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, spec);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                 BadPaddingException | IllegalBlockSizeException |
                 NoSuchPaddingException e) {
            System.err.println("Error saat enkripsi: " + e.getMessage());
            return null;
        }
    }

    /**
     * Mengubah teks tersandi kembali ke teks asli (Dekripsi).
     * @param encryptedValue Teks tersandi dalam format Base64
     * @return Teks asli
     */
    public static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec spec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, spec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException |
                 IllegalBlockSizeException | NoSuchPaddingException e) {
            System.err.println("Error saat dekripsi: " + e.getMessage());
            return null;
        }
    }
}
