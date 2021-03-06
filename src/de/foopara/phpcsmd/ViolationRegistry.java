package de.foopara.phpcsmd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.netbeans.spi.tasklist.PushTaskScanner.Callback;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import de.foopara.phpcsmd.exec.phpcpd.PhpcpdResult;
import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;

/**
 *
 * @author nspecht
 */
public class ViolationRegistry
{

    private static volatile ViolationRegistry instance = null;

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

    LinkedHashMap<String, List<FileObject>> phpcpdDependencies = new LinkedHashMap<String, List<FileObject>>();

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
            if (fo != null) {
                this.put(fo, list.get(key), this.phpcpd);
                GenericAnnotationBuilder.updateAnnotations(fo);
            }
        }
    }

    public void addPhpcpdDependency(FileObject f1, FileObject f2) {
        String p1 = f1.getPath();
        String p2 = f2.getPath();

        if (!this.phpcpdDependencies.containsKey(p1)) {
            this.phpcpdDependencies.put(p1, new ArrayList<FileObject>());
        }
        if (!this.phpcpdDependencies.containsKey(p2)) {
            this.phpcpdDependencies.put(p2, new ArrayList<FileObject>());
        }

        FileObject file1 = FileUtil.toFileObject(new File(p1));
        FileObject file2 = FileUtil.toFileObject(new File(p2));
        List<FileObject> l1 = this.phpcpdDependencies.get(p1);
        List<FileObject> l2 = this.phpcpdDependencies.get(p2);

        if (l1 != null && !l1.contains(file2)) {
            l1.add(file2);
        }
        if (l2 != null && !l2.contains(file1)) {
            l2.add(file1);
        }
    }

    public List<FileObject> getPhpcpdDependency(FileObject f) {
        List<FileObject> ret = this.phpcpdDependencies.get(f.getPath());
        if (ret == null) {
            ret = new ArrayList<FileObject>();
        }
        return ret;
    }

    public void flushPhpcpdDependency(FileObject f) {
        if (this.phpcpdDependencies.containsKey(f.getPath())) {
            this.phpcpdDependencies.get(f.getPath()).clear();
        }
    }

    public void setCallback(FileObject fo, Callback clbk) {
        if (fo == null) {
            return;
        }
        if (clbk == null) {
            return;
        }
        if (!this.callbacks.containsKey(fo.getPath())) {
            this.callbacks.put(fo.getPath(), clbk);
        }
    }

    public Integer getViolationsCount(FileObject fo) {
        return this.get(fo, this.phpcs).getSum()
                + this.get(fo, this.phpmd).getSum()
                + this.get(fo, this.phpcpd).getSum();
    }

    private void put(FileObject fo, GenericResult res, LinkedHashMap<String, GenericResult> list) {
        if (fo == null) {
            return;
        }
        if (res == null) {
            return;
        }
        if (list == null) {
            return;
        }
        //Detach old result
        if (list.containsKey(fo.getPath())) {
            GenericResult oldres = list.get(fo.getPath());
            for (int i = 0; i < oldres.getWarnings().size(); i++) {
                GenericViolation v = oldres.getWarnings().get(i);
                v.detachMe();
                v.detachChildren();
            }
            for (int i = 0; i < oldres.getErrors().size(); i++) {
                GenericViolation v = oldres.getErrors().get(i);
                v.detachMe();
                v.detachChildren();
            }
            for (int i = 0; i < oldres.getNoTask().size(); i++) {
                GenericViolation v = oldres.getNoTask().get(i);
                v.detachMe();
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
            clbk = fo.getLookup().lookup(Callback.class);
            if (clbk == null) {
                return;
            } else {
                this.callbacks.put(fo.getPath(), clbk);
            }
        }
//        clbk.started();
        clbk.clearAllTasks();

//        if (ListenerToggleDisplay.showAnnotations) {
            clbk.setTasks(fo, this.getTaskList(fo));
//        }
//        clbk.finished();
    }

    public ArrayList<Task> getTaskList(FileObject fo) {
//        if (!ListenerToggleDisplay.showAnnotations) {
//            return new ArrayList<Task>();
//        }

        if (fo == null) {
            return this.getTaskListAll();
        }
        return this.getTaskListFile(fo);
    }

    public ArrayList<Task> getTaskListFile(FileObject fo) {
        ArrayList<Task> list = new ArrayList<Task>();

        this.appendTaskList(fo, list, this.phpcs);
        this.appendTaskList(fo, list, this.phpmd);
        this.appendTaskList(fo, list, this.phpcpd);
        return list;
    }

    public ArrayList<Task> getTaskListAll() {
        ArrayList<Task> list = new ArrayList<Task>();

        for (String file : this.phpcs.keySet()) {
            this.appendTaskList(FileUtil.toFileObject(new File(file)), list, this.phpcs);
        }

        for (String file : this.phpmd.keySet()) {
            this.appendTaskList(FileUtil.toFileObject(new File(file)), list, this.phpmd);
        }

        for (String file : this.phpcpd.keySet()) {
            this.appendTaskList(FileUtil.toFileObject(new File(file)), list, this.phpcpd);
        }

        return list;
    }

    private void appendTaskList(
            FileObject fo,
            ArrayList<Task> dst,
            LinkedHashMap<String, GenericResult> registry) {

        if (registry == null) {
            return;
        }
        if (fo == null) {
            return;
        }
        if (dst == null) {
            return;
        }

        if (registry.containsKey(fo.getPath())) {
            for (GenericViolation res : registry.get(fo.getPath()).getWarnings()) {
                Task task = Task.create(
                        fo,
                        res.getTaskGroup(),
                        res.getShortDescription(),
                        res.getBeginLine() + 1);
                dst.add(task);
            }

            for (GenericViolation res : registry.get(fo.getPath()).getErrors()) {
                Task task = Task.create(
                        fo,
                        res.getTaskGroup(),
                        res.getShortDescription(),
                        res.getBeginLine() + 1);
                dst.add(task);
            }
        }
    }

    public boolean fileWasScanned(FileObject fo) {
        return this.callbacks.containsKey(fo.getPath())
                || this.phpcs.containsKey(fo.getPath())
                || this.phpmd.containsKey(fo.getPath())
                || this.phpcpd.containsKey(fo.getPath());
    }

    public void removeFile(FileObject fo) {
        if (fo == null) {
            return;
        }
        //remove violations
        this.put(fo, new GenericResult(null, null, null), phpcs);
        this.put(fo, new GenericResult(null, null, null), phpmd);
        this.put(fo, new GenericResult(null, null, null), phpcpd);

        //remove files from lists
        this.phpcs.remove(fo.getPath());
        this.phpmd.remove(fo.getPath());
        this.phpcpd.remove(fo.getPath());
        this.callbacks.remove(fo.getPath());

        this.flushPhpcpdDependency(fo);
        this.phpcpdDependencies.remove(fo.getPath());
    }

}