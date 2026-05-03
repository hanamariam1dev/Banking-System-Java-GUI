import java.awt.Color;

/**
 * ColorTheme - Defines light and dark color palettes for the CBE Banking System.
 * Works in conjunction with BankingConfig and ThemeManager.
 */
public class ColorTheme {

    public enum Mode { LIGHT, DARK }

    // ── Light Theme ──────────────────────────────────────────────────────────
    public static final Color LIGHT_PRIMARY       = new Color(0,   100, 50);
    public static final Color LIGHT_PRIMARY_LIGHT = new Color(0,   150, 75);
    public static final Color LIGHT_PRIMARY_DARK  = new Color(0,    60, 30);
    public static final Color LIGHT_PRIMARY_SOFT  = new Color(230, 246, 238);

    public static final Color LIGHT_BG       = new Color(245, 248, 246);
    public static final Color LIGHT_CARD_BG  = new Color(255, 255, 255);
    public static final Color LIGHT_FG       = new Color(10,   25, 15);
    public static final Color LIGHT_MUTED    = new Color(80,  100, 85);
    public static final Color LIGHT_BORDER   = new Color(210, 230, 218);
    public static final Color LIGHT_DIVIDER  = new Color(225, 240, 232);

    public static final Color LIGHT_SIDEBAR_BG     = new Color(8,   45, 25);
    public static final Color LIGHT_SIDEBAR_ACTIVE = new Color(0,  120, 60);
    public static final Color LIGHT_SIDEBAR_TEXT   = new Color(190, 235, 210);
    public static final Color LIGHT_SIDEBAR_ICON   = new Color(20,  210, 110);

    // ── Dark Theme ───────────────────────────────────────────────────────────
    public static final Color DARK_PRIMARY       = new Color(0,   180, 90);
    public static final Color DARK_PRIMARY_LIGHT = new Color(0,   210, 110);
    public static final Color DARK_PRIMARY_DARK  = new Color(0,   120, 60);
    public static final Color DARK_PRIMARY_SOFT  = new Color(20,   50, 35);

    public static final Color DARK_BG       = new Color(18,  28, 22);
    public static final Color DARK_CARD_BG  = new Color(28,  42, 34);
    public static final Color DARK_FG       = new Color(220, 240, 228);
    public static final Color DARK_MUTED    = new Color(130, 165, 145);
    public static final Color DARK_BORDER   = new Color(45,   70, 55);
    public static final Color DARK_DIVIDER  = new Color(38,   58, 46);

    public static final Color DARK_SIDEBAR_BG     = new Color(10,  20, 14);
    public static final Color DARK_SIDEBAR_ACTIVE = new Color(0,  140, 70);
    public static final Color DARK_SIDEBAR_TEXT   = new Color(160, 220, 185);
    public static final Color DARK_SIDEBAR_ICON   = new Color(0,   200, 100);

    // ── Shared semantic colors ────────────────────────────────────────────────
    public static final Color SUCCESS = new Color(0,   160, 70);
    public static final Color DANGER  = new Color(220,  50, 50);
    public static final Color WARNING = new Color(230, 120, 10);
    public static final Color INFO    = new Color(40,  130, 220);
    public static final Color GOLD    = new Color(220, 155, 30);

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Returns the primary color for the given mode. */
    public static Color primary(Mode mode) {
        return mode == Mode.DARK ? DARK_PRIMARY : LIGHT_PRIMARY;
    }

    /** Returns the background color for the given mode. */
    public static Color background(Mode mode) {
        return mode == Mode.DARK ? DARK_BG : LIGHT_BG;
    }

    /** Returns the card background color for the given mode. */
    public static Color cardBackground(Mode mode) {
        return mode == Mode.DARK ? DARK_CARD_BG : LIGHT_CARD_BG;
    }

    /** Returns the foreground (text) color for the given mode. */
    public static Color foreground(Mode mode) {
        return mode == Mode.DARK ? DARK_FG : LIGHT_FG;
    }

    /** Returns the muted text color for the given mode. */
    public static Color muted(Mode mode) {
        return mode == Mode.DARK ? DARK_MUTED : LIGHT_MUTED;
    }

    /** Returns the border color for the given mode. */
    public static Color border(Mode mode) {
        return mode == Mode.DARK ? DARK_BORDER : LIGHT_BORDER;
    }

    /** Returns the sidebar background color for the given mode. */
    public static Color sidebarBackground(Mode mode) {
        return mode == Mode.DARK ? DARK_SIDEBAR_BG : LIGHT_SIDEBAR_BG;
    }

    /** Returns the sidebar active item color for the given mode. */
    public static Color sidebarActive(Mode mode) {
        return mode == Mode.DARK ? DARK_SIDEBAR_ACTIVE : LIGHT_SIDEBAR_ACTIVE;
    }

    /** Blends two colors by the given ratio (0.0 = full a, 1.0 = full b). */
    public static Color blend(Color a, Color b, float ratio) {
        float inv = 1f - ratio;
        return new Color(
            Math.min(255, (int)(a.getRed()   * inv + b.getRed()   * ratio)),
            Math.min(255, (int)(a.getGreen() * inv + b.getGreen() * ratio)),
            Math.min(255, (int)(a.getBlue()  * inv + b.getBlue()  * ratio))
        );
    }

    /** Returns a semi-transparent version of the given color. */
    public static Color withAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, alpha)));
    }
}
