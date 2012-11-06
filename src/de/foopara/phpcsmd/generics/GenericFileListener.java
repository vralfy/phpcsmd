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
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, fe.getFile().getLookup())) {
            GenericExecute.executeQATools(fe.getFile(), fe.getFile().getLookup());
        }
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
        //only for folders
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, fe.getFile().getLookup())) {
            GenericExecute.executeQATools(fe.getFile(), fe.getFile().getLookup());
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
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, fae.getFile().getLookup())) {
            GenericExecute.executeQATools(fae.getFile(), fae.getFile().getLookup());
        }
    }
}
