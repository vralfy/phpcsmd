package de.foopara.phpcsmd.generics;

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIDefaults;

/**
 *
 * @author nspecht
 */
public abstract class GenericOptionsPanel extends javax.swing.JPanel
{

    public abstract void load();

    public abstract void save();

    public abstract boolean hasValidValues();

    protected boolean textfieldContainsExistingFile(JTextField field) {
        boolean ret = true;
        File script = new File(field.getText());
        field.setOpaque(true);
        if (script.exists()) {
            UIDefaults defaults = javax.swing.UIManager.getDefaults();
            field.setForeground(defaults.getColor("TextField.foreground"));
            field.setBackground(defaults.getColor("TextField.background"));
            ret = false;
        } else {
            field.setForeground(Color.white);
            field.setBackground(Color.red);
        }

        return ret;
    }

    protected void chooseScriptFile(JTextField field) {
        File script = new File(field.getText());

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("PHPCS Script Location");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        if (script.exists()) {
            fc.setSelectedFile(script);
        }
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(fc.getSelectedFile().getAbsolutePath());
        }
        this.hasValidValues();
    }
}
