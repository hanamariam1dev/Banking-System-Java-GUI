
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class UserActionEditor extends DefaultCellEditor {

    private final JButton button;
    private final JTable table;
    private String currentValue;

    public UserActionEditor(JTable table) {
        super(new JCheckBox());
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.setFont(BankingConfig.uiFont(Font.BOLD, 10));
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentValue = (value == null) ? "" : value.toString();
        button.setText(currentValue);
        button.setForeground(getColorForAction(currentValue));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    private Color getColorForAction(String action) {
        if (action == null) {
            return BankingConfig.FG;
        }
        if (action.contains("Block")) {
            return BankingConfig.DANGER;
        }
        if (action.contains("Unblock")) {
            return BankingConfig.SUCCESS;
        }
        if (action.contains("Delete")) {
            return BankingConfig.DANGER;
        }
        return BankingConfig.PRIMARY;
    }
}
