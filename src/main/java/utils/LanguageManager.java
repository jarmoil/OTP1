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
    private static final ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<>(Locale.of("en", "US"));
    private static ResourceBundle resourceBundle;

    static {
        updateResourceBundle();
    }

    private LanguageManager() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static void setLocale(Locale locale) {
        currentLocale.set(locale);
        updateResourceBundle();
    }

    public static void setLocale(String languageCode) {
        Locale locale = switch (languageCode) {
            case "JA" -> Locale.of("ja", "JP");
            case "RU" -> Locale.of("ru", "RU");
            case "IR" -> Locale.of("fa", "IR");
            default -> Locale.of("en", "US");
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
