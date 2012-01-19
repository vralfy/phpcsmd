/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import java.util.List;
import java.util.ArrayList;
import org.netbeans.spi.tasklist.PushTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.netbeans.spi.tasklist.TaskScanningScope;
import org.openide.filesystems.FileObject;


/**
 *
 * @author nspecht
 */
public class ViolationRegistry extends PushTaskScanner {

    public ViolationRegistry() {
        super(
                "PHP Checkstyle and MessDetector Violations", 
                "PHP Checkstyle and MessDetector Violations", 
                null
        );
    }
    
    @Override
    public void setScope(TaskScanningScope tss, Callback clbck) {
        if (tss == null || clbck == null) {
            return;
        }
        
        int i = 0;
        for (FileObject file : tss.getLookup().lookupAll(FileObject.class)) {
            if(i >= 1) {
                return;
            }

            //clbck.setTasks(file, scan(file));
            i++;
        }
    }
    
}
