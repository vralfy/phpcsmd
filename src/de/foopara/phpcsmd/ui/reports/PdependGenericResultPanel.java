/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import java.lang.reflect.Field;
import java.util.HashMap;
import javax.swing.*;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class PdependGenericResultPanel extends JPanel {

    HashMap<String, JComponent> elements = new HashMap<String, JComponent>();

    protected void setFields(Object o) {
        for (Field f : o.getClass().getFields()) {
            try {
                this.setComponent(f.getName(), f.get(o));
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private boolean setComponent(String name, Object value) {
        if (this.elements.containsKey(name)) {
            JComponent comp = this.elements.get(name);
            if (comp.getClass().getCanonicalName().endsWith("JProgressBar")) {
                JProgressBar tmp = ((JProgressBar) comp);
                tmp.setValue((Integer)value);
                tmp.setStringPainted(true);
                tmp.setString(tmp.getValue() + "/" + tmp.getMaximum());
            } else {
                ((JLabel) comp).setText("" + value);
            }

        } else {
            if (!this.elements.containsKey("OTHERSEP")) {
                this.addSeparator("OTHERSEP", "Other");
            }
            this.addLabel(name, "");
            JLabel comp = (JLabel) this.elements.get(name);
            comp.setText("" + value);
        }
        return false;
    }

    protected void addSeparator(String name, String caption) {
        int row = this.elements.size() + 20;
        if (name == null) {
            name = "" + Math.random();
        }
        java.awt.GridBagConstraints gridBagConstraints;
        int compwith = 2;
        if (caption != null) {
            compwith = 2;
            JLabel cap = new JLabel("<html><body><b>" + caption + "</b></body></html>");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = row;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            this.add(cap, gridBagConstraints);
        }
        JSeparator ret = new JSeparator();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = row;
        gridBagConstraints.gridwidth = compwith;
        this.add(ret, gridBagConstraints);

        this.elements.put(name, ret);
    }

    protected void addLabel(String name, String caption) {
        int row = this.elements.size() + 20;

        java.awt.GridBagConstraints gridBagConstraints;

        JLabel cap = new JLabel(caption + " (" + name + ")" + ": ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = row;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        this.add(cap, gridBagConstraints);

        JLabel ret = new JLabel("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = row;
        this.add(ret, gridBagConstraints);

        this.elements.put(name, ret);
    }

    protected void addProgressbar(String name, String caption) {
        int row = this.elements.size() + 20;

        java.awt.GridBagConstraints gridBagConstraints;

        JLabel cap = new JLabel(caption + " (" + name + ")" + ": ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = row;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        this.add(cap, gridBagConstraints);

        JProgressBar ret = new JProgressBar();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = row;
        this.add(ret, gridBagConstraints);

        this.elements.put(name, ret);
    }
}
