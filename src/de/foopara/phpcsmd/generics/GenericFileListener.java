/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.option.GeneralOptions;
import org.openide.filesystems.*;

/**
 *
 * @author nspecht
 */
public class GenericFileListener implements FileChangeListener {
    @Override
    public void fileChanged(FileEvent fe) {
        System.out.println("Änderung!!!!!!");

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
