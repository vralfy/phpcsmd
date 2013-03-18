/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.option.PdependOptions;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import org.openide.util.Lookup;

public class PdependTabbedPaneUI extends BasicTabbedPaneUI
{

    private int useTabs = -1;

    private Lookup lkp;

    public PdependTabbedPaneUI(Lookup lkp) {
        super();
        this.lkp = lkp;
    }

    public void setLookup(Lookup lkp) {
        this.lkp = lkp;
    }

    @Override
    protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
        if (this.useTabs == -1) {
            this.useTabs = (Boolean)PdependOptions.load(PdependOptions.Settings.USETABS, this.lkp) ? 1 : 0;
        }

        if (this.useTabs > 0) {
            return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
        }
        return 0;
    }

}
