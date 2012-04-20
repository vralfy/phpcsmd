package de.foopara.phpcsmd.generics;

/**
 *
 * @author nspecht
 */
public abstract class GenericOptionsPanel extends javax.swing.JPanel {
    public abstract void load();
    public abstract void save();
    public abstract boolean hasValidValues();
}
