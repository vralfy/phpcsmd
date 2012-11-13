package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.option.GeneralOptions;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import org.netbeans.spi.tasklist.PushTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.netbeans.spi.tasklist.TaskScanningScope;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class ViolationReporter extends PushTaskScanner {

    private static ViolationReporter INSTANCE;

    private TaskScanningScope scope;

    private Callback callback;

    public ViolationReporter() {
        super(
                "PHP Violations",
                "PHP Checkstyle and MessDetector Violations",
                null
        );
        ViolationReporter.INSTANCE = this;
    }


    @Override
    public synchronized void setScope(TaskScanningScope tss, Callback clbck) {
        this.scope = tss;
        this.callback = clbck;

        if (clbck == null) {
            return;
        }
        clbck.clearAllTasks();

        if (tss == null) {
            return;
        }

        HashSet<FileObject> fileList = new HashSet<FileObject>();

        for (FileObject file : this.scope.getLookup().lookupAll(FileObject.class)) {
            fileList.add(file);
        }

        for (File file : this.scope.getLookup().lookupAll(File.class)) {
            fileList.add(FileUtil.toFileObject(file));
        }

        if (fileList.size() > 0) {
            for (FileObject file : fileList) {
                ViolationRegistry.getInstance().setCallback(file, clbck);
                clbck.setTasks(file, scan(file));
            }
        } else {
            clbck.setTasks(this.getList());
        }
    }

    public List<? extends Task> scan(FileObject fo) {
        Lookup lookup = GenericHelper.getFileLookup(fo);
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.UPDATEONSAVE, lookup)) {
           GenericExecute.executeQATools(fo);
        }
         return ViolationRegistry.getInstance().getTaskList(fo);
    }

    public List<? extends Task> getList() {
        return ViolationRegistry.getInstance().getTaskList(null);
    }
}