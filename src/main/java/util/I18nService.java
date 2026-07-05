package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Service Internationalization (i18n) dengan Observer Pattern.
 * Memungkinkan perubahan bahasa secara real-time di semua panel.
 */
public class I18nService {

    private static ResourceBundle bundle;
    private static Locale currentLocale;

    /**
     * Interface untuk panel/form yang ingin mendengarkan perubahan bahasa.
     */
    public interface I18nChangeListener {
        void onLanguageChanged();
    }

    private static final List<I18nChangeListener> listeners = new ArrayList<>();

    static {
        setLocale(Locale.of("id"));
    }

    /**
     * Mengubah locale dan memicu update ke semua listener.
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
        notifyListeners();
    }

    /**
     * Mengubah locale berdasarkan kode bahasa ("id", "en", "ms").
     */
    public static void setLocaleByCode(String languageCode) {
        setLocale(Locale.of(languageCode));
    }

    /**
     * Mengambil teks terjemahan berdasarkan key.
     */
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException | NullPointerException e) {
            return "!" + key + "!";
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Mendaftarkan listener untuk perubahan bahasa.
     */
    public static synchronized void registerListener(I18nChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Menghapus listener dari daftar.
     */
    public static synchronized void unregisterListener(I18nChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Memberitahu semua listener bahwa bahasa telah berubah.
     */
    private static void notifyListeners() {
        for (I18nChangeListener listener : listeners) {
            if (listener != null) {
                listener.onLanguageChanged();
            }
        }
    }
}
