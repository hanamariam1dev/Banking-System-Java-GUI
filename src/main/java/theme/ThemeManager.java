import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
public class ThemeManager {
    private static boolean darkMode = false;
    public static void applyTheme() {
        Color bg = darkMode ? new Color(18, 25, 20) : ColorTheme.BG;
        Color card = darkMode ? new Color(28, 38, 32) : ColorTheme.CARD_BG;
        Color fg = darkMode ? new Color(220, 235, 225) : ColorTheme.FG;
        Color muted = darkMode ? new Color(130, 150, 135) : ColorTheme.MUTED;
        Color border = darkMode ? new Color(50, 70, 55) : ColorTheme.BORDER;
        UIManager.put("Panel.background", bg);
        UIManager.put("ScrollPane.background", bg);
        UIManager.put("Viewport.background", bg);
        UIManager.put("ToolBar.background", card);
        UIManager.put("TextField.background", card);
        UIManager.put("TextField.foreground", fg);
        UIManager.put("TextField.caretForeground", fg);
        UIManager.put("PasswordField.background", card);
        UIManager.put("PasswordField.foreground", fg);
        UIManager.put("Button.background", ColorTheme.PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
    }
    public static Font uiFont(int style, int size) {
        String fontName = LanguageManager.isAmharic() ? "Nyala" : "SansSerif";
        if (fontName.equals("Nyala")) {
            try {
                Font f = new Font("Nyala", style, size);
                if (f.canDisplay('ሀ')) return f;
            } catch (Exception e) {
            }
        }
        return new Font("SansSerif", style, size);
    }
    public static void setDarkMode(boolean dark) {
        darkMode = dark;
    }
    public static boolean isDarkMode() {
        return darkMode;
    }
}