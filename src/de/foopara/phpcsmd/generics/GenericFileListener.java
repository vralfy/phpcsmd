package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.FileListenerRegistry;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.option.GeneralOptions;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class GenericFileListener implements FileChangeListener {
    protected Lookup lkp;

    public GenericFileListener(Lookup lkp) {
        this.lkp = lkp;
    }

    @Override
    public void fileChanged(FileEvent fe) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, this.lkp)) {
            GenericExecute.executeQATools(fe.getFile(), this.lkp);
        }
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
        //only for folders
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        //only for folders
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        ViolationRegistry.getInstance().removeFile(fe.getFile());
        FileListenerRegistry.getInstance().removeListener(fe.getFile().getPath());
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, this.lkp)) {
            GenericExecute.executeQATools(fae.getFile(), this.lkp);
        }
    }
}
