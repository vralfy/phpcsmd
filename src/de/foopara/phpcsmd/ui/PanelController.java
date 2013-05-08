package de.foopara.phpcsmd.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JComponent;

import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class PanelController extends OptionsPanelController
{

    private boolean changed = false;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public void update() {
        MainPanel.getInstance().load();
        this.changed = false;
    }

    @Override
    public void applyChanges() {
        MainPanel.getInstance().save();
        this.changed = false;
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isValid() {
        return MainPanel.getInstance().hasValidValues();
    }

    @Override
    public boolean isChanged() {
        return this.changed;
    }

    @Override
    public JComponent getComponent(Lookup lkp) {
        //Momentan noch CodesnifferPanel, später dann großes Frame mit allen Panels
        return new MainPanel();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        this.pcs.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        this.pcs.removePropertyChangeListener(pl);
    }

}
