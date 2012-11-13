package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.FileListenerRegistry;
import de.foopara.phpcsmd.ViolationRegistry;
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

    @Override
    public void fileChanged(FileEvent fe) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, GenericHelper.getFileLookup(fe.getFile()))) {
            GenericExecute.executeQATools(fe.getFile());
        }
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
        //only for folders
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, GenericHelper.getFileLookup(fe.getFile()))) {
            GenericExecute.executeQATools(fe.getFile());
        }
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        ViolationRegistry.getInstance().removeFile(fe.getFile());
        FileListenerRegistry.getInstance().removeListener(fe.getFile().getPath());
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
        ViolationRegistry.getInstance().removeFile(fre.getFile());
        FileListenerRegistry.getInstance().removeListener(fre.getFile().getPath());
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, GenericHelper.getFileLookup(fae.getFile()))) {
            GenericExecute.executeQATools(fae.getFile());
        }
    }
}
