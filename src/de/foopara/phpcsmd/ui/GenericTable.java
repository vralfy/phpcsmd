/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui;

import java.util.Comparator;
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

    public static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 < o2) return -1;
            else if (o1 > o2) return 1;
            return 0;
        }
    }

    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public static class FilepathComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] s1 = o1.split("/|\\/");
            String[] s2 = o2.split("/|\\/");

            int mindepth = Math.min(s1.length, s2.length) - 1;
            if (mindepth > 0) {
                for (int i=0;i<mindepth;i++) {
                    int comp = s1[i].compareTo(s2[i]);
                    if (comp != 0) return comp;
                }
            }
            
            if (s1.length < s2.length) return -1;
            else if (s1.length > s2.length) return 1;

            return o1.compareTo(o2);
        }
    }

    public GenericTable() {
        super();
        this.acr = AdvancedCellRenderer.setCellRenderer(this);

        this.model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int a, int b) {
                return false;
            }
        };
        this.setModel(this.model);
    }

    protected void finishTableSettings() {
        this.sorter = new TableRowSorter<DefaultTableModel>(this.model);
        this.sorter.setModel(this.model);

        this.setRowSorter(this.sorter);
        this.setAutoCreateRowSorter(false);
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
