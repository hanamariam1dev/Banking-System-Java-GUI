import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
public class CBELogoPanel extends JPanel {
    private Image logoImage;
    public CBELogoPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(180, 180));
        String[] paths = {
            "image.png",
            "Java Project/image.png",
            System.getProperty("user.dir") + "/image.png",
            System.getProperty("user.dir") + "/Java Project/image.png",
        };
        for (String path : paths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                logoImage = new ImageIcon(f.getAbsolutePath()).getImage();
                break;
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        if (logoImage != null) {
            Ellipse2D clip = new Ellipse2D.Float(0, 0, w, h);
            g2.setClip(clip);
            g2.drawImage(logoImage, 0, 0, w, h, this);
            g2.setClip(null);
            g2.setColor(new Color(215, 145, 20));
            g2.setStroke(new BasicStroke(Math.max(2f, w * 0.04f)));
            g2.drawOval(1, 1, w - 2, h - 2);
            return;
        }
        int cx = w / 2, cy = h / 2;
        int r = Math.min(w, h) / 2 - 2;
        g2.setColor(new Color(215, 145, 20));
        g2.setStroke(new BasicStroke(Math.max(2f, r * 0.06f)));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        int ri = (int)(r * 0.88f);
        g2.setColor(new Color(0, 45, 20));
        g2.fillOval(cx - ri, cy - ri, ri * 2, ri * 2);
        g2.setColor(new Color(255, 255, 255, 18));
        g2.fillArc(cx - ri, cy - ri, ri * 2, ri * 2, 0, 180);
        int fontSize = (int)(ri * 0.62f);
        g2.setFont(ThemeManager.uiFont(Font.BOLD, fontSize));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String txt = "CBE";
        g2.drawString(txt, cx - fm.stringWidth(txt) / 2,
            cy + (fm.getAscent() - fm.getDescent()) / 2 - (int)(ri * 0.06f));
        int lineY = cy + (fm.getAscent() - fm.getDescent()) / 2 + (int)(ri * 0.10f);
        g2.setColor(new Color(215, 145, 20));
        g2.setStroke(new BasicStroke(Math.max(1f, ri * 0.04f),
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx - fm.stringWidth(txt) / 2, lineY,
                    cx + fm.stringWidth(txt) / 2, lineY);
    }
}