import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
public class ImageUtils {
    public static Image loadImage(String path, int width, int height) {
        try {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image img = icon.getImage();
                return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BufferedImage createCircularImage(BufferedImage image, int size) {
        BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();
        return output;
    }
    public static boolean saveProfileImage(String accountNumber, File sourceFile) {
        try {
            BufferedImage img = ImageIO.read(sourceFile);
            if (img == null) return false;
            String destPath = "profiles/" + accountNumber + ".png";
            File destDir = new File("profiles");
            if (!destDir.exists()) destDir.mkdirs();
            BufferedImage circular = createCircularImage(img, 200);
            return ImageIO.write(circular, "PNG", new File(destPath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ImageIcon getProfileImage(String accountNumber, int size) {
        String[] paths = {
            "profiles/" + accountNumber + ".png",
            "profiles/default.png"
        };
        for (String path : paths) {
            Image img = loadImage(path, size, size);
            if (img != null) {
                return new ImageIcon(img);
            }
        }
        return null;
    }
}