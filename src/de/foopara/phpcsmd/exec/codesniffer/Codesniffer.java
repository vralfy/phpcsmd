/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.codesniffer;

import de.foopara.phpcsmd.generics.GenericExecute;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
public class Codesniffer extends GenericExecute {
   private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected void run(FileObject file, boolean annotations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
