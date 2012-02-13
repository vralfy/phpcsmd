/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nspecht
 */
public class GenericTable extends JTable {
    protected AdvancedCellRenderer acr = null;
    protected TableRowSorter<DefaultTableModel> sorter = null;
    protected DefaultTableModel model = null;

    public GenericTable() {
        super();
        sorter = new TableRowSorter<DefaultTableModel>();
        this.acr = AdvancedCellRenderer.setCellRenderer(this);
        this.setRowSorter(sorter);
        this.setAutoCreateRowSorter(true);

        this.model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int a, int b) {
                return false;
            }
        };

        this.setModel(this.model);
    }

    public void flushElements() {
        this.clearSelection();
        this.setCellSelectionEnabled(false);
        int size = this.model.getRowCount()-1;
        if (size > 0) {
            this.model.getDataVector().removeAllElements();
            this.model.fireTableRowsDeleted(0, size);
        }
        this.setCellSelectionEnabled(true);
    }
}
