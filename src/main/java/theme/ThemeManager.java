import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * ThemeManager - Central manager for applying and switching themes
 * across the CBE Banking System UI.
 *
 * Usage:
 *   ThemeManager.getInstance().applyTheme(ColorTheme.Mode.DARK);
 *   ThemeManager.getInstance().addListener(mode -> refreshMyPanel());
 */
public class ThemeManager {

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static ThemeManager instance;

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    // ── State ─────────────────────────────────────────────────────────────────
    private ColorTheme.Mode currentMode = ColorTheme.Mode.LIGHT;
    private final List<ThemeChangeListener> listeners = new ArrayList<>();

    private ThemeManager() {}

    // ── Public API ────────────────────────────────────────────────────────────

    /** Returns the currently active theme mode. */
    public ColorTheme.Mode getCurrentMode() {
        return currentMode;
    }

    /** Returns true if dark mode is currently active. */
    public boolean isDarkMode() {
        return currentMode == ColorTheme.Mode.DARK;
    }

    /**
     * Applies the given theme mode and notifies all registered listeners.
     * Must be called on the Event Dispatch Thread.
     */
    public void applyTheme(ColorTheme.Mode mode) {
        this.currentMode = mode;
        syncToBankingConfig(mode);
        notifyListeners(mode);
    }

    /** Toggles between light and dark mode. */
    public void toggleTheme() {
        applyTheme(isDarkMode() ? ColorTheme.Mode.LIGHT : ColorTheme.Mode.DARK);
    }

    /**
     * Registers a listener that will be called whenever the theme changes.
     * Listeners are called on the Event Dispatch Thread.
     */
    public void addListener(ThemeChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Removes a previously registered theme change listener. */
    public void removeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    // ── Convenience color accessors ───────────────────────────────────────────

    public Color primary()          { return ColorTheme.primary(currentMode); }
    public Color background()       { return ColorTheme.background(currentMode); }
    public Color cardBackground()   { return ColorTheme.cardBackground(currentMode); }
    public Color foreground()       { return ColorTheme.foreground(currentMode); }
    public Color muted()            { return ColorTheme.muted(currentMode); }
    public Color border()           { return ColorTheme.border(currentMode); }
    public Color sidebarBackground(){ return ColorTheme.sidebarBackground(currentMode); }
    public Color sidebarActive()    { return ColorTheme.sidebarActive(currentMode); }

    // ── Theme application helpers ─────────────────────────────────────────────

    /**
     * Applies the current theme colors to a JPanel and all its children
     * recursively.
     */
    public void applyToComponent(JComponent component) {
        if (component == null) return;
        component.setBackground(background());
        component.setForeground(foreground());
        for (Component child : component.getComponents()) {
            if (child instanceof JComponent) {
                applyToComponent((JComponent) child);
            }
        }
    }

    /**
     * Applies theme-appropriate colors to a JButton.
     *
     * @param button   the button to style
     * @param primary  true for a filled primary button, false for a ghost button
     */
    public void styleButton(JButton button, boolean primary) {
        if (primary) {
            button.setBackground(ColorTheme.primary(currentMode));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(background());
            button.setForeground(ColorTheme.primary(currentMode));
            button.setBorder(BorderFactory.createLineBorder(ColorTheme.primary(currentMode), 1, true));
        }
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    /**
     * Applies theme-appropriate colors to a JLabel.
     *
     * @param label  the label to style
     * @param muted  true for secondary/muted text, false for primary text
     */
    public void styleLabel(JLabel label, boolean muted) {
        label.setForeground(muted ? ColorTheme.muted(currentMode) : ColorTheme.foreground(currentMode));
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    /**
     * Keeps BankingConfig in sync so that existing code that reads
     * BankingConfig colors continues to work after a theme switch.
     */
    private void syncToBankingConfig(ColorTheme.Mode mode) {
        BankingConfig.darkMode = (mode == ColorTheme.Mode.DARK);

        BankingConfig.PRIMARY       = ColorTheme.primary(mode);
        BankingConfig.PRIMARY_LIGHT = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_PRIMARY_LIGHT : ColorTheme.LIGHT_PRIMARY_LIGHT;
        BankingConfig.PRIMARY_DARK  = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_PRIMARY_DARK  : ColorTheme.LIGHT_PRIMARY_DARK;
        BankingConfig.PRIMARY_SOFT  = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_PRIMARY_SOFT  : ColorTheme.LIGHT_PRIMARY_SOFT;

        BankingConfig.BG       = ColorTheme.background(mode);
        BankingConfig.CARD_BG  = ColorTheme.cardBackground(mode);
        BankingConfig.FG       = ColorTheme.foreground(mode);
        BankingConfig.MUTED    = ColorTheme.muted(mode);
        BankingConfig.BORDER   = ColorTheme.border(mode);
        BankingConfig.DIVIDER  = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_DIVIDER : ColorTheme.LIGHT_DIVIDER;

        BankingConfig.SIDEBAR_BG     = ColorTheme.sidebarBackground(mode);
        BankingConfig.SIDEBAR_ACTIVE = ColorTheme.sidebarActive(mode);
        BankingConfig.SIDEBAR_TEXT   = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_SIDEBAR_TEXT : ColorTheme.LIGHT_SIDEBAR_TEXT;
        BankingConfig.SIDEBAR_ICON   = (mode == ColorTheme.Mode.DARK)
            ? ColorTheme.DARK_SIDEBAR_ICON : ColorTheme.LIGHT_SIDEBAR_ICON;

        // Keep legacy aliases in sync
        BankingConfig.PURPLE       = BankingConfig.PRIMARY;
        BankingConfig.PURPLE_LIGHT = BankingConfig.PRIMARY_LIGHT;
        BankingConfig.PURPLE_DARK  = BankingConfig.PRIMARY_DARK;
        BankingConfig.PURPLE_SOFT  = BankingConfig.PRIMARY_SOFT;
    }

    private void notifyListeners(ColorTheme.Mode mode) {
        for (ThemeChangeListener l : listeners) {
            SwingUtilities.invokeLater(() -> l.onThemeChanged(mode));
        }
    }

    // ── Listener interface ────────────────────────────────────────────────────

    /** Callback interface for theme change events. */
    @FunctionalInterface
    public interface ThemeChangeListener {
        void onThemeChanged(ColorTheme.Mode newMode);
    }
}
