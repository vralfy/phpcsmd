package de.foopara.phpcsmd.generics;

import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

public abstract class GenericTopComponent extends TopComponent
{

    abstract public void setCommandOutput(String output);

    protected Lookup lkp = null;

    public void setLookup(Lookup lkp) {
        this.lkp = lkp;
    }

}
