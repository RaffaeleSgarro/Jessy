package jessy.utils;

import javafx.scene.Scene;

public class StyleUtils {
    
    public static void addAllStylesheetsToScene(Scene scene) {
        String[] resources = { "/style/style.css" };
        for (String res : resources) {
            scene.getStylesheets().add(StyleUtils.class.getResource(res).toExternalForm());
        }
    }
}
