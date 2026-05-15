import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
public class ActionCellRenderer extends JPanel implements TableCellRenderer {
    private final JLabel lbl = new JLabel();
    public ActionCellRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        lbl.setFont(ThemeManager.uiFont(Font.BOLD, 11));
        lbl.setForeground(ColorTheme.PRIMARY);
        add(lbl);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        lbl.setText(value != null ? value.toString() : "");
        return this;
    }
}