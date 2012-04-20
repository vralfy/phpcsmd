package de.foopara.phpcsmd.ui.reports;

import java.util.Collections;
import java.util.Comparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 *
 * @author n.specht
 */
public class PdependTreeNode extends DefaultMutableTreeNode {

    public PdependTreeNode(Object o) {
        super(o);
    }

    protected Comparator nodeComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == null || o1.toString() == null) {return -1;}
            if (o2 == null || o2.toString() == null) {return  1;}
            return o1.toString().compareToIgnoreCase(o2.toString());
        }
    };

    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
        Collections.sort(this.children, this.nodeComparator);
    }
}
