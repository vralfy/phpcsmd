/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
abstract public class GenericExecute {
    public abstract boolean isEnabled();
    protected abstract void run(FileObject file, boolean annotations);
    
    protected void run(FileObject file) {
        this.run(file, false);
    }
    
    public void execute(FileObject file) {
        this.run(file, false);
    }
    
    public void execute(FileObject file, boolean annotations) {
        this.run(file, annotations);
    }
}
