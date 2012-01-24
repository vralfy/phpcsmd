/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.netbeans.spi.tasklist.PushTaskScanner.Callback;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;

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

    LinkedHashMap<String, Callback> callbacks = new LinkedHashMap<String, Callback>();

    public void setPhpcs(FileObject fo, GenericResult res) {
        this.put(fo, res, this.phpcs);
        GenericAnnotationBuilder.run(fo, res);
    }

    public void setPhpmd(FileObject fo, GenericResult res) {
        this.put(fo, res, this.phpmd);
        GenericAnnotationBuilder.run(fo, res);
    }

    public void setCallback(FileObject fo, Callback clbk) {
        this.callbacks.put(fo.getPath(), clbk);
    }

    private void put(FileObject fo, GenericResult res, LinkedHashMap<String, GenericResult> list) {
        if (list.containsKey(fo.getPath())) {
            GenericResult oldres = list.get(fo.getPath());
            for (int i=0;i<oldres.getWarnings().size(); i++) {
                GenericViolation v = oldres.getWarnings().get(i);
                v.detach();
                v.detachChildren();
            }
            for (int i=0;i<oldres.getErrors().size(); i++) {
                GenericViolation v = oldres.getErrors().get(i);
                v.detach();
                v.detachChildren();
            }
        }
        list.put(fo.getPath(), res);
    }

    public void reprintTasks(FileObject fo) {
        if (fo == null) return;
        Callback clbk = this.callbacks.get(fo.getPath());
        if (clbk == null) return;
        clbk.clearAllTasks();
        clbk.setTasks(fo, this.getTaskList(fo));
    }

    public ArrayList<Task> getTaskList(FileObject fo) {
        ArrayList<Task> list = new ArrayList<Task>();
        if (fo == null) return list;
        this.appendTaskList(fo, list, this.phpcs);
        this.appendTaskList(fo, list, this.phpmd);
        return list;
    }

    private void appendTaskList(
            FileObject fo,
            ArrayList<Task> dst,
            LinkedHashMap<String, GenericResult> registry
    ) {
        if (registry.containsKey(fo.getPath())) {
            for (GenericViolation res : registry.get(fo.getPath()).getWarnings()) {
                dst.add(
                    Task.create(
                        fo,
                        res.getAnnotationType(),
                        res.getShortDescription(),
                        res.getBeginLine() + 1
                        ));
            }

            for (GenericViolation res : registry.get(fo.getPath()).getErrors()) {
                dst.add(
                    Task.create(
                        fo,
                        res.getAnnotationType(),
                        res.getShortDescription(),
                        res.getBeginLine() + 1
                        ));
            }
        }
    }
}
