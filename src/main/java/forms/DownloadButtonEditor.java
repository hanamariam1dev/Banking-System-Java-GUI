import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
public class DownloadButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final JTable table;
    private String currentValue;
    private Runnable downloadAction;
    public DownloadButtonEditor(JTable table) {
        super(new JCheckBox());
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.setFont(ThemeManager.uiFont(Font.BOLD, 10));
        button.setForeground(ColorTheme.PRIMARY);
        button.addActionListener(e -> {
            if (downloadAction != null) {
                downloadAction.run();
            }
            fireEditingStopped();
        });
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                  boolean isSelected, int row, int column) {
        currentValue = (value == null) ? "" : value.toString();
        button.setText(currentValue);
        return button;
    }
    public void setDownloadAction(Runnable action) {
        this.downloadAction = action;
    }
    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }
}