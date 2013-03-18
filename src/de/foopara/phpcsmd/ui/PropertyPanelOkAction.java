/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropertyPanelOkAction implements ActionListener
{

    PropertyPanel panel = null;

    public void setPanel(PropertyPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.panel.save();
    }

}
