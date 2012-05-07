package de.foopara.phpcsmd;

import de.foopara.phpcsmd.exec.phpcpd.PhpcpdResult;
import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.netbeans.spi.tasklist.PushTaskScanner.Callback;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author nspecht
 */
public class ViolationRegistry {

    private static ViolationRegistry instance = null;

    public static ViolationRegistry getInstance() {
        if (ViolationRegistry.instance == null) {
            ViolationRegistry.instance = new ViolationRegistry();
        }
        return ViolationRegistry.instance;
    }

    LinkedHashMap<String, GenericResult> phpcs = new LinkedHashMap<String, GenericResult>();
    LinkedHashMap<String, GenericResult> phpmd = new LinkedHashMap<String, GenericResult>();
    LinkedHashMap<String, GenericResult> phpcpd = new LinkedHashMap<String, GenericResult>();
    LinkedHashMap<String, Callback> callbacks = new LinkedHashMap<String, Callback>();
    LinkedHashMap<FileObject, List<FileObject>> phpcpdDependencies = new LinkedHashMap<FileObject, List<FileObject>>();

    public void setPhpcs(FileObject fo, GenericResult res) {
        this.put(fo, res, this.phpcs);
    }

    public GenericResult getPhpcs(FileObject fo) {
        return this.get(fo, this.phpcs);
    }

    public void setPhpmd(FileObject fo, GenericResult res) {
        this.put(fo, res, this.phpmd);
    }

    public GenericResult getPhpmd(FileObject fo) {
        return this.get(fo, this.phpmd);
    }

    public void setPhpcpd(FileObject fo, GenericResult res) {
        this.put(fo, res, this.phpcpd);
    }

    public GenericResult getPhpcpd(FileObject fo) {
        return this.get(fo, this.phpcpd);
    }

    public void setPhpcpdFolder(HashMap<String, PhpcpdResult> list) {
        for (String key : list.keySet()) {
            FileObject fo = FileUtil.toFileObject(new File(key));
            if (fo!=null) {
                this.put(fo, list.get(key), this.phpcpd);
                GenericAnnotationBuilder.updateAnnotations(fo);
            }
        }
    }

    public void addPhpcpdDependency(FileObject f1, FileObject f2) {
        if (!this.phpcpdDependencies.containsKey(f1)) {
            this.phpcpdDependencies.put(f1, new ArrayList<FileObject>());
        }
        if (!this.phpcpdDependencies.containsKey(f2)) {
            this.phpcpdDependencies.put(f2, new ArrayList<FileObject>());
        }

        this.phpcpdDependencies.get(f1).add(f2);
        this.phpcpdDependencies.get(f2).add(f1);
    }

    public List<FileObject> getPhpcpdDependency(FileObject f) {
         List<FileObject> ret = this.phpcpdDependencies.get(f);
         if (ret == null) {
             ret = new ArrayList<FileObject>();
         }
         return ret;
    }

    public void flushPhpcpdDependency(FileObject f) {
        if (this.phpcpdDependencies.containsKey(f)) {
            this.phpcpdDependencies.get(f).clear();
        }
    }

    public void setCallback(FileObject fo, Callback clbk) {
        this.callbacks.put(fo.getPath(), clbk);
    }

    public Integer getViolationsCount(FileObject fo) {
        return this.get(fo, this.phpcs).getSum() +
                this.get(fo, this.phpmd).getSum() +
                this.get(fo, this.phpcpd).getSum()
                ;
    }

    private void put(FileObject fo, GenericResult res, LinkedHashMap<String, GenericResult> list) {
        //Detach old result
        if (list.containsKey(fo.getPath())) {
            GenericResult oldres = list.get(fo.getPath());
            for (int i = 0; i < oldres.getWarnings().size(); i++) {
                GenericViolation v = oldres.getWarnings().get(i);
                v.detach();
                v.detachChildren();
            }
            for (int i = 0; i < oldres.getErrors().size(); i++) {
                GenericViolation v = oldres.getErrors().get(i);
                v.detach();
                v.detachChildren();
            }
            for (int i = 0; i < oldres.getNoTask().size(); i++) {
                GenericViolation v = oldres.getNoTask().get(i);
                v.detach();
                v.detachChildren();
            }
            oldres.getWarnings().clear();
            oldres.getErrors().clear();
            oldres.getNoTask().clear();
        }
        
        //Add new result (will be attached later)
        list.remove(fo.getPath());
        list.put(fo.getPath(), res);
    }

    private GenericResult get(FileObject fo, LinkedHashMap<String, GenericResult> list) {
        if (list.containsKey(fo.getPath())) {
            return list.get(fo.getPath());
        }
        return new GenericResult(null, null, null);
    }

    public void reprintTasks(FileObject fo) {
        if (fo == null) {
            return;
        }
        Callback clbk = this.callbacks.get(fo.getPath());
        if (clbk == null) {
            return;
        }
        clbk.clearAllTasks();
        clbk.setTasks(fo, this.getTaskList(fo));
    }

    public ArrayList<Task> getTaskList(FileObject fo) {
        ArrayList<Task> list = new ArrayList<Task>();
        if (fo == null) {
            return list;
        }
        this.appendTaskList(fo, list, this.phpcs);
        this.appendTaskList(fo, list, this.phpmd);
        this.appendTaskList(fo, list, this.phpcpd);
        return list;
    }

    private void appendTaskList(
            FileObject fo,
            ArrayList<Task> dst,
            LinkedHashMap<String, GenericResult> registry) {
        if (registry.containsKey(fo.getPath())) {
            for (GenericViolation res : registry.get(fo.getPath()).getWarnings()) {
                dst.add(
                        Task.create(
                            fo,
                            res.getTaskGroup(),
                            res.getShortDescription(),
                            res.getBeginLine() + 1));
            }

            for (GenericViolation res : registry.get(fo.getPath()).getErrors()) {
                dst.add(
                        Task.create(
                            fo,
                            res.getTaskGroup(),
                            res.getShortDescription(),
                            res.getBeginLine() + 1));
            }
        }
    }

}