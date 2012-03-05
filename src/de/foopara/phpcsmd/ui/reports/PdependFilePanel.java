/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author nspecht
 */
public class PdependFilePanel extends PdependGenericResultPanel {

    /**
     * Creates new form PdependFilePanel
     */
    public PdependFilePanel() {
        super();
        initComponents();
        this.addLabel("name", null, "File");
        ((JLabel)this.elements.get("name")).setVisible(false);
        this.addSeparator(null, "Codelines", "File");
        this.addLabel("loc", "Lines of Code", "File");
        this.addProgressbar("ncloc", "Non Comment Lines of Code", "File");
        this.addProgressbar("cloc", "Comment Lines of Code", "File");
        this.addProgressbar("eloc", "Executable Lines of Code", "File");
        this.addProgressbar("lloc", "Logical Lines Of Code", "File");
    }

    public void setFile(PdependTypes.PdependFile file) {
        ((JProgressBar)this.elements.get("ncloc")).setMaximum(file.loc);
        ((JProgressBar)this.elements.get("cloc")).setMaximum(file.loc);
        ((JProgressBar)this.elements.get("eloc")).setMaximum(file.loc);
        ((JProgressBar)this.elements.get("lloc")).setMaximum(file.loc);

        this.setFields(file);
        this.setEditorButton(file.name);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
