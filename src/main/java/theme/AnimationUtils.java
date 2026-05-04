import javax.swing.*;
import java.awt.*;
public class AnimationUtils {
    public static void fadeIn(JComponent component, int duration) {
        Timer timer = new Timer(16, null);
        final float[] alpha = {0f};
        timer.addActionListener(e -> {
            alpha[0] += 0.05f;
            if (alpha[0] >= 1f) {
                alpha[0] = 1f;
                timer.stop();
            }
            component.repaint();
        });
        timer.start();
    }
    public static void slideIn(JComponent component, int direction, int distance) {
        Timer timer = new Timer(16, null);
        final int[] current = {0};
        timer.addActionListener(e -> {
            current[0] += distance / 20;
            if (Math.abs(current[0]) >= Math.abs(distance)) {
                current[0] = distance;
                timer.stop();
            }
            component.repaint();
        });
        timer.start();
    }
    public static javax.swing.Timer createPulseAnimation(JComponent component, Color baseColor) {
        javax.swing.Timer timer = new javax.swing.Timer(100, null);
        final float[] pulse = {0f};
        timer.addActionListener(e -> {
            pulse[0] += 0.1f;
            if (pulse[0] > Math.PI * 2) pulse[0] = 0;
            int alpha = (int)(128 + 127 * Math.sin(pulse[0]));
            component.setForeground(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha));
        });
        return timer;
    }
}