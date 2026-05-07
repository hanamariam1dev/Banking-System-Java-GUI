
import javax.swing.table.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserTableModel extends AbstractTableModel {

    private final String[] columns = {"Account", "Name", "Type", "Status", "Balance", "Actions"};
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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }

    public void addRow(Object[] row) {
        data.add(row);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void clear() {
        data.clear();
        fireTableDataChanged();
    }

    public String getAccountAt(int row) {
        return (String) data.get(row)[0];
    }

    public void loadUsers() {
        clear();
        try {
            Connection con = ConnectionManager.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT account_number, account_holder_name, account_type, status, balance "
                    + "FROM accounts WHERE role = 'Customer' ORDER BY created_at DESC");
            while (rs.next()) {
                Object[] row = {
                    rs.getString("account_number"),
                    rs.getString("account_holder_name"),
                    rs.getString("account_type"),
                    rs.getString("status"),
                    CurrencyFormatter.format(rs.getDouble("balance")),
                    "Manage"
                };
                addRow(row);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
