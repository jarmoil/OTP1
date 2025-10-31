package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages application language settings and resource bundles.
 *
 *
 */
public class LanguageManager {
    private static final String BUNDLE_BASE_NAME = "MessagesBundle";
    private static final ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<>(new Locale("en", "US"));
    private static ResourceBundle resourceBundle;

    static {
        updateResourceBundle();
    }

    public static void setLocale(Locale locale) {
        currentLocale.set(locale);
        updateResourceBundle();
    }

    public static void setLocale(String languageCode) {
        Locale locale = switch (languageCode) {
            case "JA" -> new Locale("ja", "JP");
            case "RU" -> new Locale("ru", "RU");
            default -> new Locale("en", "US");
        };
        setLocale(locale);
    }


    public static Locale getCurrentLocale() {
        return currentLocale.get();
    }

    public static ObjectProperty<Locale> currentLocaleProperty() {
        return currentLocale;
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    private static void updateResourceBundle() {
        resourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, currentLocale.get());
    }
}
