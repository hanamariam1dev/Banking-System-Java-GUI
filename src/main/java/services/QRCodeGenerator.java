
import java.awt.*;
import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    public static BufferedImage generate(String text, int size) {
        String qrData = createScannableQRData(text);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, size, size);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            return generateFallbackQR(text, size);
        }
    }

    private static String createScannableQRData(String inputData) {
        String[] parts = inputData.split("\\|");
        if (parts.length < 3) {
            return "BANK: CBE\nACCOUNT: 000000000000\nNAME: Unknown";
        }
        String accountNumber = parts[1].replaceAll("\\D", "");
        String accountHolder = parts[2].trim();
        return "BANK: CBE\n"
                + "ACCOUNT: " + accountNumber + "\n"
                + "NAME: " + accountHolder;
    }

    private static void generateScannableQRCode(Graphics2D g2, String data, int size) {
        int modules = 21;
        int moduleSize = size / modules;
        int border = (size - modules * moduleSize) / 2;
        boolean[][] matrix = createMinimalQRMatrix(data, modules);
        for (int y = 0; y < modules; y++) {
            for (int x = 0; x < modules; x++) {
                if (matrix[y][x]) {
                    g2.fillRect(border + x * moduleSize, border + y * moduleSize,
                            moduleSize, moduleSize);
                }
            }
        }
    }

    private static boolean[][] createMinimalQRMatrix(String data, int modules) {
        boolean[][] matrix = new boolean[modules][modules];
        addMinimalFinderPattern(matrix, 0, 0);
        addMinimalFinderPattern(matrix, modules - 7, 0);
        addMinimalFinderPattern(matrix, 0, modules - 7);
        addMinimalTimingPatterns(matrix, modules);
        matrix[13][8] = true;
        addMinimalFormatInfo(matrix, modules);
        encodeMinimalData(matrix, data, modules);
        return matrix;
    }

    private static void addMinimalFinderPattern(boolean[][] matrix, int startX, int startY) {
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                boolean isOuter = x == 0 || x == 6 || y == 0 || y == 6;
                boolean isInner = x >= 2 && x <= 4 && y >= 2 && y <= 4;
                matrix[startY + y][startX + x] = isOuter || isInner;
            }
        }
    }

    private static void addMinimalTimingPatterns(boolean[][] matrix, int modules) {
        for (int i = 8; i < modules - 8; i++) {
            matrix[6][i] = (i % 2) == 0;
            matrix[i][6] = (i % 2) == 0;
        }
    }

    private static void addMinimalFormatInfo(boolean[][] matrix, int modules) {
        matrix[8][0] = true;
        matrix[8][1] = false;
        matrix[8][2] = true;
        matrix[8][3] = true;
        matrix[8][4] = false;
        matrix[8][5] = true;
        matrix[8][6] = false;
        matrix[8][7] = true;
        matrix[0][8] = true;
        matrix[1][8] = false;
        matrix[2][8] = true;
        matrix[3][8] = true;
        matrix[4][8] = false;
        matrix[5][8] = true;
        matrix[6][8] = false;
        matrix[7][8] = true;
        matrix[modules - 1][8] = false;
        matrix[modules - 2][8] = true;
        matrix[modules - 3][8] = false;
        matrix[modules - 4][8] = true;
        matrix[8][modules - 1] = false;
        matrix[8][modules - 2] = true;
        matrix[8][modules - 3] = false;
        matrix[8][modules - 4] = true;
    }

    private static void encodeMinimalData(boolean[][] matrix, String data, int modules) {
        String bitString = "0100";
        String charCount = String.format("%8s", Integer.toBinaryString(data.length())).replace(' ', '0');
        bitString += charCount;
        for (char c : data.toCharArray()) {
            bitString += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
        }
        bitString += "0000";
        while (bitString.length() % 8 != 0) {
            bitString += "0";
        }
        int[] paddingBytes = {236, 17};
        int paddingIndex = 0;
        while (bitString.length() < 152) {
            bitString += String.format("%8s", Integer.toBinaryString(paddingBytes[paddingIndex])).replace(' ', '0');
            paddingIndex = 1 - paddingIndex;
        }
        placeMinimalBitsInMatrix(matrix, bitString, modules);
    }

    private static void placeMinimalBitsInMatrix(boolean[][] matrix, String bitString, int modules) {
        int bitIndex = 0;
        int direction = -1;
        int x = modules - 1;
        int y = modules - 1;
        while (x > 0 && bitIndex < bitString.length()) {
            if (x == 6) {
                x--;
            }
            for (int i = 0; i < 2 && bitIndex < bitString.length(); i++) {
                if (!isMinimalFunctionArea(x, y, modules)) {
                    matrix[y][x] = bitString.charAt(bitIndex) == '1';
                    bitIndex++;
                }
                y += direction;
                if (y < 0 || y >= modules) {
                    direction = -direction;
                    y += direction;
                    break;
                }
            }
            x--;
            if (x < 0) {
                break;
            }
        }
    }

    private static boolean isMinimalFunctionArea(int x, int y, int modules) {
        if ((x < 7 && y < 7) || (x >= modules - 7 && y < 7) || (x < 7 && y >= modules - 7)) {
            return true;
        }
        if (x == 6 || y == 6) {
            return true;
        }
        if (x == 8 && y == 13) {
            return true;
        }
        if ((x <= 8 && y == 8) || (x == 8 && y <= 8)) {
            return true;
        }
        return false;
    }

    private static BufferedImage generateFallbackQR(String text, int size) {
        String qrData = createScannableQRData(text);
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, size, size);
        g2.setColor(Color.BLACK);
        generateBasicQRCode(g2, qrData, size);
        g2.dispose();
        return img;
    }

    private static void generateBasicQRCode(Graphics2D g2, String data, int size) {
        int modules = 21;
        int moduleSize = size / modules;
        int border = (size - modules * moduleSize) / 2;
        for (int y = 0; y < modules; y++) {
            for (int x = 0; x < modules; x++) {
                int hash = data.hashCode() + x * 31 + y * 37;
                boolean isBlack = (hash & (1 << (x % 8))) != 0;
                if ((x < 7 && y < 7) || (x >= modules - 7 && y < 7) || (x < 7 && y >= modules - 7)) {
                    isBlack = true;
                } else if (x == 6 || y == 6) {
                    isBlack = (x + y) % 2 == 0;
                }
                if (isBlack) {
                    g2.fillRect(border + x * moduleSize, border + y * moduleSize, moduleSize, moduleSize);
                }
            }
        }
    }
}
