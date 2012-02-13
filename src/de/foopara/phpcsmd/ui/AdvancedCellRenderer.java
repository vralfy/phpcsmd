/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nspecht
 */
public class AdvancedCellRenderer extends DefaultTableCellRenderer {

    private JTable table;

    public static AdvancedCellRenderer setCellRenderer(JTable t) {
        AdvancedCellRenderer ret = new AdvancedCellRenderer();
        ret.setTable(t);
        return ret;
    }

    public void setTable(JTable t) {
        this.table = t;
        this.table.setDefaultRenderer(Object.class, this);

        this.table.setCellSelectionEnabled(false);
        this.table.setColumnSelectionAllowed(false);
        this.table.setRowSelectionAllowed(true);

        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) this.renderInteger((Integer)value);
        else {
            setIcon(null);
            super.setValue(value);
        }
    }

    private void renderInteger(Integer value) {
        String font = value.toString();
        
        if (value > 0) {
            font = "<b style=\"color:#FF0000:\">"+font+"</b>";
        }
        setText("<html><body>"+font+"</body></html>");
        setIcon(null);
    }
}
