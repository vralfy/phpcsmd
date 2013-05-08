package de.foopara.phpcsmd.ui;

import java.io.Serializable;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nspecht
 */
public class GenericTable extends JTable
{

    public static class GenericTableModel extends DefaultTableModel
    {
        public static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int a, int b) {
            return false;
        }

    }

    public static class IntegerComparator implements Comparator<Integer>, Serializable
    {
        public static final long serialVersionUID = 1L;

        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 < o2) {
                return -1;
            } else if (o1 > o2) {
                return 1;
            }
            return 0;
        }

    }

    public static class StringComparator implements Comparator<String>, Serializable
    {
        public static final long serialVersionUID = 1L;

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }

    }

    public static class FilepathComparator implements Comparator<String>, Serializable
    {
        public static final long serialVersionUID = 1L;

        @Override
        public int compare(String o1, String o2) {
            String[] s1 = o1.split("/|\\/");
            String[] s2 = o2.split("/|\\/");

            int mindepth = Math.min(s1.length, s2.length) - 1;
            if (mindepth > 0) {
                for (int i = 0; i < mindepth; i++) {
                    int comp = s1[i].compareTo(s2[i]);
                    if (comp != 0) {
                        return comp;
                    }
                }
            }

            if (s1.length < s2.length) {
                return -1;
            } else if (s1.length > s2.length) {
                return 1;
            }

            return o1.compareTo(o2);
        }

    }

    protected AdvancedCellRenderer acr = null;

    protected TableRowSorter<GenericTableModel> sorter = null;

    protected GenericTableModel model = null;

    public GenericTable() {
        super();
        this.acr = AdvancedCellRenderer.setCellRenderer(this);

        this.model = new GenericTableModel();
        this.setModel(this.model);
    }

    protected void finishTableSettings() {
        this.sorter = new TableRowSorter<GenericTableModel>(this.model);
        this.sorter.setModel(this.model);

        this.setRowSorter(this.sorter);
        this.setAutoCreateRowSorter(false);
    }

    public void flushElements() {
        this.clearSelection();
        this.setCellSelectionEnabled(false);
        int size = this.model.getRowCount() - 1;
        if (size > 0) {
            this.model.getDataVector().removeAllElements();
            this.model.fireTableRowsDeleted(0, size);
        }
        this.setCellSelectionEnabled(true);
    }

}
