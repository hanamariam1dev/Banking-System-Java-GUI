
import javax.swing.table.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Date", "Type", "Amount", "Balance", "Description"};
    private List<Object[]> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    public void addRow(Object[] row) {
        data.add(row);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void clear() {
        data.clear();
        fireTableDataChanged();
    }

    public void loadTransactions(String accountNumber) {
        clear();
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT transaction_id, transaction_date, transaction_type, amount, "
                    + "balance_after, description FROM transactions "
                    + "WHERE account_number = ? ORDER BY transaction_date DESC LIMIT 100");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("transaction_id"),
                    LocaleConfig.formatTimestamp(rs.getTimestamp("transaction_date")),
                    rs.getString("transaction_type"),
                    CurrencyFormatter.format(rs.getDouble("amount")),
                    CurrencyFormatter.format(rs.getDouble("balance_after")),
                    rs.getString("description")
                };
                addRow(row);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
