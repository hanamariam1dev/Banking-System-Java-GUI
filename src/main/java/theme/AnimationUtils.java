import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * AnimationUtils - Reusable Swing animation helpers for the CBE Banking System.
 *
 * Provides:
 *  - fadeIn / fadeOut for any JComponent
 *  - slideIn for panels (from left, right, top, or bottom)
 *  - pulse effect for buttons (draw attention)
 *  - smooth color transition between two colors
 *  - easing functions (ease-in-out, bounce)
 */
public class AnimationUtils {

    /** Frames per second for all animations. */
    public static final int FPS = 60;

    /** Default animation duration in milliseconds. */
    public static final int DEFAULT_DURATION_MS = 300;

    // ── Fade ──────────────────────────────────────────────────────────────────

    /**
     * Fades a component in from fully transparent to fully opaque.
     *
     * @param component  the component to animate (must be inside a JLayeredPane
     *                   or have a parent that supports AlphaComposite painting)
     * @param durationMs animation duration in milliseconds
     * @param onDone     optional callback invoked on the EDT when done (may be null)
     */
    public static void fadeIn(JComponent component, int durationMs, Runnable onDone) {
        animate(component, 0f, 1f, durationMs, alpha -> {
            component.putClientProperty("alpha", alpha);
            component.repaint();
        }, onDone);
    }

    /**
     * Fades a component out from fully opaque to fully transparent.
     *
     * @param component  the component to animate
     * @param durationMs animation duration in milliseconds
     * @param onDone     optional callback invoked on the EDT when done (may be null)
     */
    public static void fadeOut(JComponent component, int durationMs, Runnable onDone) {
        animate(component, 1f, 0f, durationMs, alpha -> {
            component.putClientProperty("alpha", alpha);
            component.repaint();
        }, onDone);
    }

    // ── Slide ─────────────────────────────────────────────────────────────────

    /** Direction constants for slideIn. */
    public enum Direction { LEFT, RIGHT, TOP, BOTTOM }

    /**
     * Slides a component into its natural position from the given direction.
     * The component must already be laid out at its target bounds.
     *
     * @param component  the component to animate
     * @param direction  the direction to slide from
     * @param durationMs animation duration in milliseconds
     * @param onDone     optional callback invoked on the EDT when done (may be null)
     */
    public static void slideIn(JComponent component, Direction direction,
                               int durationMs, Runnable onDone) {
        Rectangle target = component.getBounds();
        int offsetX = 0, offsetY = 0;
        switch (direction) {
            case LEFT:   offsetX = -target.width;  break;
            case RIGHT:  offsetX =  target.width;  break;
            case TOP:    offsetY = -target.height; break;
            case BOTTOM: offsetY =  target.height; break;
        }
        final int startX = target.x + offsetX;
        final int startY = target.y + offsetY;
        animate(component, 0f, 1f, durationMs, t -> {
            float eased = easeInOut(t);
            int x = (int)(startX + (target.x - startX) * eased);
            int y = (int)(startY + (target.y - startY) * eased);
            component.setLocation(x, y);
        }, onDone);
    }

    // ── Pulse ─────────────────────────────────────────────────────────────────

    /**
     * Briefly scales a button up then back to normal to draw attention.
     * Uses a border-based visual cue compatible with all Swing L&Fs.
     *
     * @param button     the button to pulse
     * @param color      the pulse highlight color
     * @param durationMs total duration of one pulse cycle in milliseconds
     */
    public static void pulse(JButton button, Color color, int durationMs) {
        Color original = button.getBackground();
        animate(button, 0f, 1f, durationMs, t -> {
            float intensity = (float) Math.sin(t * Math.PI); // 0 → 1 → 0
            Color blended = ColorTheme.blend(original, color, intensity * 0.4f);
            button.setBackground(blended);
            button.repaint();
        }, () -> {
            button.setBackground(original);
            button.repaint();
        });
    }

    // ── Color transition ──────────────────────────────────────────────────────

    /**
     * Smoothly transitions a component's background from one color to another.
     *
     * @param component  the component whose background to animate
     * @param from       starting color
     * @param to         ending color
     * @param durationMs animation duration in milliseconds
     * @param onDone     optional callback invoked on the EDT when done (may be null)
     */
    public static void colorTransition(JComponent component, Color from, Color to,
                                       int durationMs, Runnable onDone) {
        animate(component, 0f, 1f, durationMs, t -> {
            float eased = easeInOut(t);
            component.setBackground(ColorTheme.blend(from, to, eased));
            component.repaint();
        }, onDone);
    }

    // ── Theme switch animation ────────────────────────────────────────────────

    /**
     * Animates a full-window theme switch by briefly overlaying a flash panel.
     * Call this before applying the new theme for a smooth visual transition.
     *
     * @param window     the root window to animate
     * @param durationMs total duration in milliseconds
     * @param onMidpoint callback invoked at the midpoint (apply theme here)
     * @param onDone     callback invoked when animation completes (may be null)
     */
    public static void animateThemeSwitch(JFrame window, int durationMs,
                                          Runnable onMidpoint, Runnable onDone) {
        JLayeredPane layered = window.getLayeredPane();
        JPanel overlay = new JPanel() {
            float alpha = 0f;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
            public void setAlpha(float a) { this.alpha = a; repaint(); }
        };
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, layered.getWidth(), layered.getHeight());
        layered.add(overlay, JLayeredPane.DRAG_LAYER);

        int half = durationMs / 2;
        // Phase 1: fade to black
        animate(overlay, 0f, 1f, half, t -> {
            ((JPanel) overlay).putClientProperty("alpha", t);
            overlay.repaint();
        }, () -> {
            // Midpoint: apply theme
            if (onMidpoint != null) onMidpoint.run();
            // Phase 2: fade back out
            animate(overlay, 1f, 0f, half, t -> {
                overlay.putClientProperty("alpha", t);
                overlay.repaint();
            }, () -> {
                layered.remove(overlay);
                layered.repaint();
                if (onDone != null) onDone.run();
            });
        });
    }

    // ── Easing functions ──────────────────────────────────────────────────────

    /** Smooth ease-in-out (cubic). Input and output in [0, 1]. */
    public static float easeInOut(float t) {
        return t < 0.5f
            ? 4f * t * t * t
            : 1f - (float) Math.pow(-2f * t + 2f, 3) / 2f;
    }

    /** Ease-in (quadratic). Input and output in [0, 1]. */
    public static float easeIn(float t) {
        return t * t;
    }

    /** Ease-out (quadratic). Input and output in [0, 1]. */
    public static float easeOut(float t) {
        return 1f - (1f - t) * (1f - t);
    }

    /** Simple bounce ease-out. Input and output in [0, 1]. */
    public static float bounceOut(float t) {
        if (t < 1f / 2.75f) {
            return 7.5625f * t * t;
        } else if (t < 2f / 2.75f) {
            t -= 1.5f / 2.75f;
            return 7.5625f * t * t + 0.75f;
        } else if (t < 2.5f / 2.75f) {
            t -= 2.25f / 2.75f;
            return 7.5625f * t * t + 0.9375f;
        } else {
            t -= 2.625f / 2.75f;
            return 7.5625f * t * t + 0.984375f;
        }
    }

    // ── Core animation engine ─────────────────────────────────────────────────

    /**
     * Core timer-based animation loop.
     *
     * @param component  component to repaint (used only to anchor the timer)
     * @param from       start value (0.0 – 1.0)
     * @param to         end value (0.0 – 1.0)
     * @param durationMs total duration in milliseconds
     * @param onFrame    called each frame with the current interpolated value
     * @param onDone     called on the EDT when the animation completes (may be null)
     */
    public static void animate(JComponent component, float from, float to,
                                int durationMs, FloatConsumer onFrame, Runnable onDone) {
        if (durationMs <= 0) {
            onFrame.accept(to);
            if (onDone != null) SwingUtilities.invokeLater(onDone);
            return;
        }

        int delay = Math.max(1, 1000 / FPS);
        long startTime = System.currentTimeMillis();

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float progress = Math.min(1f, (float) elapsed / durationMs);
                float value = from + (to - from) * progress;
                onFrame.accept(value);
                if (progress >= 1f) {
                    timer.stop();
                    if (onDone != null) SwingUtilities.invokeLater(onDone);
                }
            }
        });
        timer.start();
    }

    // ── Functional interface ──────────────────────────────────────────────────

    /** Simple float consumer to avoid requiring Java 8 java.util.function. */
    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);
    }
}
