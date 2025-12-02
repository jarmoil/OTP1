package utils;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class FXMLLoaderUtil {

    private FXMLLoaderUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static FXMLLoader createLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(FXMLLoaderUtil.class.getResource(fxmlPath));
        loader.setResources(LanguageManager.getResourceBundle());
        return loader;
    }

    public static <T> T load(String fxmlPath) throws IOException {
        return createLoader(fxmlPath).load();
    }
}
