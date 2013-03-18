package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
import de.foopara.phpcsmd.option.PdependOptions;
import javax.swing.JProgressBar;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class PdependClassPanel extends PdependGenericResultPanel
{

    /**
     * Creates new form PdependClassPanel
     */
    public PdependClassPanel(Lookup lkp) {
        super(lkp);
        initComponents();
        this.addLabel("name", "Name", "Class");
        this.addLabel("nocc", "Number of Child Classes", "Class");
        this.addLabel("csz", "Class Size", "Class");
        this.addLabel("cis", "Class Interface Size", "Class");
        this.addLabel("dit", "Depth of Inheritance Tree", "Class");

        if ((Boolean)PdependOptions.load(PdependOptions.Settings.USETABS, this.lkp) == false) {
            this.addSeparator(null, "Codelines", "Codelines");
        }
        this.addLabel("loc", "Lines of Code", "Codelines");
        this.addProgressbar("ncloc", "Non Comment Lines of Code", "Codelines");
        this.addProgressbar("cloc", "Comment Lines of Code", "Codelines");
        this.addProgressbar("eloc", "Executable Lines of Code", "Codelines");
        this.addProgressbar("lloc", "Logical Lines Of Code", "Codelines");

        if ((Boolean)PdependOptions.load(PdependOptions.Settings.USETABS, this.lkp) == false) {
            this.addSeparator(null, "Methods", "Methods");
        }
        this.addLabel("nom", "Number of Methods", "Methods");
        this.addProgressbar("noam", "Number of Added Methods", "Methods");
        this.addProgressbar("noom", "Number of Overwritten Methods", "Methods");
        this.addProgressbar("npm", "Number of Public Methods", "Methods");

        if ((Boolean)PdependOptions.load(PdependOptions.Settings.USETABS, this.lkp) == false) {
            this.addSeparator(null, "Properties", "Properties");
        }
        this.addLabel("vars", "Number of Properties", "Properties");
        this.addProgressbar("varsi", "Number of Inherited Properties", "Properties");
        this.addProgressbar("varsnp", "Number of Non Private Properties", "Properties");

        this.addSeparator(null, "Coupling", "Different Metrics");
        this.addLabel("ca", "Afferent Coupling", "Different Metrics");
        this.addLabel("cbo", "Coupling Between Objects", "Different Metrics");
        this.addLabel("ce", "Efferent Coupling", "Different Metrics");


        this.addSeparator(null, "Code Rank", "Different Metrics");
        this.addLabel("cr", "Code Rank", "Different Metrics");
        this.addLabel("rcr", "Reverse Code Rank", "Different Metrics");

        this.addSeparator(null, "Weighted Method Count", "Different Metrics");
        this.addLabel("wmc", "Weighted Method Count", "Different Metrics");
        this.addLabel("wmci", "Inherited Weighted Method Count", "Different Metrics");
        this.addLabel("wmcnp", "Non Private Weighted Method Count", "Different Metrics");
    }

    public void setClass(PdependTypes.PdependClass clss) {
        ((JProgressBar)this.elements.get("ncloc")).setMaximum(clss.loc);
        ((JProgressBar)this.elements.get("cloc")).setMaximum(clss.loc);
        ((JProgressBar)this.elements.get("eloc")).setMaximum(clss.loc);
        ((JProgressBar)this.elements.get("lloc")).setMaximum(clss.loc);

        ((JProgressBar)this.elements.get("noam")).setMaximum(clss.nom);
        ((JProgressBar)this.elements.get("noom")).setMaximum(clss.nom);
        ((JProgressBar)this.elements.get("npm")).setMaximum(clss.nom);

        ((JProgressBar)this.elements.get("varsi")).setMaximum(clss.vars);
        ((JProgressBar)this.elements.get("varsnp")).setMaximum(clss.vars);
        this.setFields(clss);
        this.setEditorButton(clss.getFilename());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
