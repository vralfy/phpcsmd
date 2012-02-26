/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependResult;
import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
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
    private String _filter;

    public PdependTree() {
        this.setResult(null);
    }

    public void setResult(PdependResult res) {
        this.nodeRoot = new DefaultMutableTreeNode("metrics");
        this.nodeFiles = new DefaultMutableTreeNode("files");
        this.nodePackages = new DefaultMutableTreeNode("packages");
        this.nodeRoot.add(this.nodeFiles);
        this.nodeRoot.add(this.nodePackages);

        this.model = new DefaultTreeModel(this.nodeRoot);
        this.setModel(this.model);

        if (res != null) {
            this.nodeRoot.setUserObject(res.getMetrics());
            for (Object o : res.getFiles().toArray()) {
                PdependTypes.PdependFile _file = (PdependTypes.PdependFile) o;
                _file.setFilter(this._filter);
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(_file);
                this.nodeFiles.add(n);
            }

            for (Object o : res.getPackages().toArray()) {
                PdependTypes.PdependPackage _package = (PdependTypes.PdependPackage) o;
                DefaultMutableTreeNode _packageNode = new DefaultMutableTreeNode(_package);
                this.nodePackages.add(_packageNode);

                for (PdependTypes.PdependClass _class : _package.getClasses()) {
                    DefaultMutableTreeNode _classNode = new DefaultMutableTreeNode(_class);
                    _packageNode.add(_classNode);
                    for (PdependTypes.PdependMethod _method : _class.getMethods()) {
                        DefaultMutableTreeNode _methodNode = new DefaultMutableTreeNode(_method);
                        _classNode.add(_methodNode);
                    }
                }
            }
        }
    }

    public void setFilter(String f) {
        this._filter = f;
    }

    public Object getSelectedItem() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        if (node == null) return null;
        return node.getUserObject();
    }
}
