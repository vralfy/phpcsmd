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
        this.phpcs.put(fo.getPath(), res);
        GenericAnnotationBuilder.run(fo, res);
    }

    public void setPhpmd(FileObject fo, GenericResult res) {
        this.phpmd.put(fo.getPath(), res);
        GenericAnnotationBuilder.run(fo, res);
    }

    public void setCallback(FileObject fo, Callback clbk) {
        this.callbacks.put(fo.getPath(), clbk);
    }

    public void reprintTasks(FileObject fo) {
        Callback clbk = this.callbacks.get(fo.getPath());
        if (clbk == null) return;
        clbk.setTasks(fo, this.getTaskList(fo));
    }

    public ArrayList<Task> getTaskList(FileObject fo) {
        ArrayList<Task> list = new ArrayList<Task>();
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
