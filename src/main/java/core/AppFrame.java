import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class AppFrame extends JFrame {
    private static AppFrame instance;
    public static AppFrame get() {
        if (instance == null) {
            instance = new AppFrame();
        }
        return instance;
    }
    private AppFrame() {
        setTitle("CBE Banking System");
        setSize(1100, 700);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    public void showPage(JPanel page, String title) {
        setTitle(title);
        setContentPane(page);
        revalidate();
        repaint();
        if (!isVisible()) setVisible(true);
    }
    public void showPage(JPanel page) {
        showPage(page, getTitle());
    }
}