package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependResult;
import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

public class PdependTree extends JTree
{

    private DefaultTreeModel model;

    private PdependTreeNode nodeRoot;

    private PdependTreeNode nodeFiles;

    private PdependTreeNode nodePackages;

    private PdependTreeNode nodeFunctions;

    private String _filter;

    public PdependTree() {
        this.setResult(null);
    }

    public void setResult(PdependResult res) {
        this.nodeRoot = new PdependTreeNode("metrics");
        this.nodeFiles = new PdependTreeNode("files");
        this.nodePackages = new PdependTreeNode("packages");
        this.nodeFunctions = new PdependTreeNode("functions");

        this.model = new DefaultTreeModel(this.nodeRoot);
        this.setModel(this.model);

        if (res != null) {
            this.nodeRoot.setUserObject(res.getMetrics());

            for (Object o : res.getFiles().toArray()) {
                PdependTypes.PdependFile _file = (PdependTypes.PdependFile)o;
                _file.setFilter(this._filter);
                PdependTreeNode n = new PdependTreeNode(_file);
                this.nodeFiles.add(n);
                if (this.nodeFiles.getParent() == null) {
                    this.nodeRoot.add(this.nodeFiles);
                }
            }

            for (Object o : res.getPackages().toArray()) {
                PdependTypes.PdependPackage _package = (PdependTypes.PdependPackage)o;
                PdependTreeNode _packageNode = new PdependTreeNode(_package);
                this.nodePackages.add(_packageNode);
                if (this.nodePackages.getParent() == null) {
                    this.nodeRoot.add(this.nodePackages);
                }

                for (PdependTypes.PdependClass _class : _package.getClasses()) {
                    PdependTreeNode _classNode = new PdependTreeNode(_class);
                    _packageNode.add(_classNode);
                    for (PdependTypes.PdependMethod _method : _class.getMethods()) {
                        PdependTreeNode _methodNode = new PdependTreeNode(_method);
                        _classNode.add(_methodNode);
                    }
                }
            }

            for (Object o : res.getFunctions().toArray()) {
                PdependTypes.PdependFunction _function = (PdependTypes.PdependFunction)o;
                PdependTreeNode n = new PdependTreeNode(_function);
                this.nodeFunctions.add(n);
                if (this.nodeFunctions.getParent() == null) {
                    this.nodeRoot.add(this.nodeFunctions);
                }
            }
        }
    }

    public void setFilter(String f) {
        this._filter = f;
    }

    public Object getSelectedItem() {
        PdependTreeNode node = (PdependTreeNode)this.getLastSelectedPathComponent();
        if (node == null) {
            return null;
        }
        return node.getUserObject();
    }

}
