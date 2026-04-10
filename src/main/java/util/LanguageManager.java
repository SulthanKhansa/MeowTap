package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    
    // Ini buat nampung kamus yang lagi aktif
    private static ResourceBundle bundle;

    // Fungsi buat ngeset bahasa (dipanggil pas tombol bendera diklik di UI)
    public static void setLanguage(String languageCode, String countryCode) {
        // Bikin aturan lokasinya (misal "id", "ID" atau "en", "US")
        Locale locale = new Locale(languageCode, countryCode);
        
        // Load file properties dari package "lang" yang namanya depannya "messages"
        bundle = ResourceBundle.getBundle("lang.messages", locale);
        System.out.println("Sip, bahasa udah diganti ke " + languageCode + " wkwk");
    }

    // Fungsi buat ngambil teks dari kamus
    public static String getString(String key) {
        // Kalo belom ada bahasa yang dipilih, paksa pake Bahasa Indonesia
        if (bundle == null) {
            setLanguage("id", "ID");
        }
        
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            // Kalo kuncinya ga ketemu di kamus, balikin kuncinya aja biar ga error
            return key; 
        }
    }
}