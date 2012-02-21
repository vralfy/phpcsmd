/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author n.specht
 */
public class PdependTree extends JTree {
    private DefaultTreeModel model;
    private DefaultMutableTreeNode nodeRoot;
    private DefaultMutableTreeNode nodeFiles;
    private DefaultMutableTreeNode nodePackages;

    public PdependTree() {
        this.nodeRoot = new DefaultMutableTreeNode("metrics");
        this.nodeFiles = new DefaultMutableTreeNode("files");
        this.nodePackages = new DefaultMutableTreeNode("packages");
        this.nodeRoot.add(this.nodeFiles);
        this.nodeRoot.add(this.nodePackages);

        this.model = new DefaultTreeModel(this.nodeRoot);
        this.setModel(this.model);
    }
}
