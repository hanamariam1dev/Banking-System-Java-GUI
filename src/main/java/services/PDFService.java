
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PDFService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void generateReceiptPDF(int transactionId, String filePath) {
        StringBuilder content = new StringBuilder();
        content.append("COMMERCIAL BANK OF ETHIOPIA\n");
        content.append("TRANSACTION RECEIPT\n\n");
        appendTransactionDetails(content, transactionId);
        content.append("\nThank you for banking with CBE\n");
        writeSimplePdf(filePath, content.toString());
    }

    private static void appendTransactionDetails(StringBuilder content, int transactionId) {
        Connection con = BankingConfig.con;
        if (con == null) {
            content.append("Transaction ID: ").append(transactionId).append("\n");
            content.append("Status: Unable to connect to database\n");
            content.append("Date: ").append(DATE_FORMAT.format(new java.util.Date())).append("\n");
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT t.*, a.account_holder_name "
                    + "FROM transactions t "
                    + "LEFT JOIN accounts a ON t.account_number = a.account_number "
                    + "WHERE t.transaction_id = ?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                content.append("Transaction ID:   ").append(rs.getInt("transaction_id")).append("\n");
                content.append("Account Number:   ").append(rs.getString("account_number")).append("\n");
                String holder = rs.getString("account_holder_name");
                content.append("Account Holder:   ").append(holder != null ? holder : "Unknown").append("\n");
                content.append("Transaction Type: ").append(rs.getString("transaction_type")).append("\n");
                content.append("Amount:           ETB ").append(String.format("%,.2f", rs.getDouble("amount"))).append("\n");
                content.append("Balance After:    ETB ").append(String.format("%,.2f", rs.getDouble("balance_after"))).append("\n");
                String recipient = rs.getString("recipient_account");
                if (recipient != null && !recipient.isEmpty()) {
                    content.append("Recipient:        ").append(recipient).append("\n");
                }
                String desc = rs.getString("description");
                if (desc != null && !desc.isEmpty()) {
                    content.append("Description:      ").append(desc).append("\n");
                }
                content.append("Date:             ").append(DATE_FORMAT.format(rs.getTimestamp("transaction_date"))).append("\n");
                content.append("Status:           Completed\n");
            } else {
                content.append("Transaction ID: ").append(transactionId).append("\n");
                content.append("Status: No transaction found for this ID\n");
                content.append("Date: ").append(DATE_FORMAT.format(new java.util.Date())).append("\n");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            content.append("Transaction ID: ").append(transactionId).append("\n");
            content.append("Status: Error retrieving details — ").append(e.getMessage()).append("\n");
            content.append("Date: ").append(DATE_FORMAT.format(new java.util.Date())).append("\n");
        }
    }

    public static void generateCustomPDF(String filePath, String customContent) {
        StringBuilder content = new StringBuilder();
        content.append("COMMERCIAL BANK OF ETHIOPIA\n");
        content.append("CUSTOM RECEIPT\n\n");
        content.append(customContent).append("\n\n");
        content.append("Thank you for banking with CBE\n");
        writeSimplePdf(filePath, content.toString());
    }

    private static void writeSimplePdf(String filePath, String text) {
        try {
            byte[] body = buildPdfBody(text);
            Files.write(Paths.get(filePath), body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] buildPdfBody(String text) throws IOException {
        String[] lines = text.split("\\r?\\n");
        byte[] contentStream = buildContents(lines);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLine(baos, "%PDF-1.4");
        long obj1 = baos.size();
        writeLine(baos, "1 0 obj");
        writeLine(baos, "<< /Type /Catalog /Pages 2 0 R >>");
        writeLine(baos, "endobj");
        long obj2 = baos.size();
        writeLine(baos, "2 0 obj");
        writeLine(baos, "<< /Type /Pages /Kids [3 0 R] /Count 1 >>");
        writeLine(baos, "endobj");
        long obj3 = baos.size();
        writeLine(baos, "3 0 obj");
        writeLine(baos, "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>");
        writeLine(baos, "endobj");
        long obj4 = baos.size();
        writeLine(baos, "4 0 obj");
        writeLine(baos, "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>");
        writeLine(baos, "endobj");
        long obj5 = baos.size();
        writeLine(baos, "5 0 obj");
        writeLine(baos, "<< /Length " + contentStream.length + " >>");
        writeLine(baos, "stream");
        baos.write(contentStream);
        writeLine(baos, "endstream");
        writeLine(baos, "endobj");
        long xref = baos.size();
        writeLine(baos, "xref");
        writeLine(baos, "0 6");
        writeLine(baos, String.format("%010d 65535 f ", 0));
        writeLine(baos, String.format("%010d 00000 n ", obj1));
        writeLine(baos, String.format("%010d 00000 n ", obj2));
        writeLine(baos, String.format("%010d 00000 n ", obj3));
        writeLine(baos, String.format("%010d 00000 n ", obj4));
        writeLine(baos, String.format("%010d 00000 n ", obj5));
        writeLine(baos, "trailer");
        writeLine(baos, "<< /Size 6 /Root 1 0 R >>");
        writeLine(baos, "startxref");
        writeLine(baos, String.valueOf(xref));
        writeLine(baos, "%%EOF");
        return baos.toByteArray();
    }

    private static byte[] buildContents(String[] lines) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n/F1 12 Tf\n50 760 Td\n");
        for (int i = 0; i < lines.length; i++) {
            sb.append("(").append(escape(lines[i])).append(") Tj\n");
            if (i < lines.length - 1) {
                sb.append("0 -16 Td\n");
            }
        }
        sb.append("ET\n");
        return sb.toString().getBytes(StandardCharsets.US_ASCII);
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void writeLine(ByteArrayOutputStream baos, String line) throws IOException {
        baos.write(line.getBytes(StandardCharsets.US_ASCII));
        baos.write('\n');
    }
}
