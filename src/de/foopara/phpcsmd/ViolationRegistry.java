/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericResult;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    
    LinkedHashMap<String, GenericResult> codesniffer = new LinkedHashMap<String, GenericResult>();
    
    public void setCodesniffer(FileObject fo, GenericResult res) {
        this.codesniffer.put(fo.getPath(), res);
        GenericAnnotationBuilder.run(fo, res);
    }
    
    public ArrayList<Task> getTaskList(FileObject fo) {
        ArrayList<Task> list = new ArrayList<Task>();
        this.appendTaskList(fo, list, this.codesniffer);
        return list;
    }
    
    private void appendTaskList(
            FileObject fo, 
            ArrayList<Task> dst, 
            LinkedHashMap<String, GenericResult> registry
    ) {
        if (registry.containsKey(fo.getPath())) {
            for (PhpCsMdWarning res : registry.get(fo.getPath()).getWarnings()) {
                dst.add(
                    Task.create(
                        fo,
                        res.getAnnotationType(),
                        res.getShortDescription(),
                        res.getLine() + 1
                        ));
            }
                
            for (PhpCsMdError res : registry.get(fo.getPath()).getErrors()) {
                dst.add(
                    Task.create(
                        fo,
                        res.getAnnotationType(),
                        res.getShortDescription(),
                        res.getLine() + 1
                        ));
            }
        }
    }
}
