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
    protected abstract GenericResult run(FileObject file, boolean annotations);
    
    protected GenericResult run(FileObject file) {
        return this.run(file, false);
    }
    
    public GenericResult execute(FileObject file) {
        return this.run(file, false);
    }
    
    public GenericResult execute(FileObject file, boolean annotations) {
        return this.run(file, annotations);
    }
}
