/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.option.GeneralOptions;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;

/**
 *
 * @author nspecht
 */
public class GenericFileListener implements FileChangeListener {

    private GenericResult _res = null;

    public void setResult(GenericResult res) {
        this._res = res;
    }

    @Override
    public void fileChanged(FileEvent fe) {
        for(GenericViolation warning: this._res.getWarnings()) {
            warning.detach();
        }
        for(GenericViolation error: this._res.getErrors()) {
            error.detach();
        }
        fe.getFile().removeFileChangeListener(this);
        if (GeneralOptions.getUpdateOnSave()) {
            GenericExecute.executeQATools(fe.getFile());
        }
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
    }

    @Override
    public void fileDeleted(FileEvent fe) {
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
    }

}
