/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.exec.phpcpd.Phpcpd;
import de.foopara.phpcsmd.exec.phpcpd.PhpcpdResult;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.io.File;
import java.util.HashMap;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author n.specht
 */
public class RescanThread extends Thread {

    private FileObject fo = null;
    private ScanReportTopComponent component = null;
    private boolean retrieveValuesFromRegistry = false;

    public void setFileObject(FileObject fo) {
        this.fo = fo;
    }

    public void setTopComponent(ScanReportTopComponent c) {
        this.component = c;
    }

    public void setRetrieveValuesFromRegistry(boolean b) {
        this.retrieveValuesFromRegistry = b;
    }

    /*
     * Nur damit Netbeans die Klappe hällt
     */
    @Override
    public void run() {
        this.qarun();
    }

    public void qarun() {
        this.count(this.fo, 0);
        this.component.setRescanDone();
    }

    private int count(FileObject f, int fc) {
        boolean firstRun = (fc == 0);
        for (FileObject f2 : f.getChildren()) {
            if (GenericHelper.isDesirableFile(f2)) {
                fc += 1;
                if (!this.retrieveValuesFromRegistry) {
                    QAThread qa = new QAThread();
                    qa.setFileObject(f2);
                    qa.setPoking(false);
//                    qa.run();
                }
                this.component.setScannedFilecount(fc);
                this.component.addElementToTable(f2);                
            } else if (f2.isFolder()) {
                fc = this.count(f2, fc);
            }
        }
        if (GenericHelper.isDesirableFolder(f) && firstRun) {
            Phpcpd cpdTask = new Phpcpd();
            HashMap<String, PhpcpdResult> res = cpdTask.runFolder(f, true);
            for (String path : res.keySet()) {
                FileObject tmp = FileUtil.toFileObject(new File(path));
                if (tmp != null) {
                    this.component.addElementToTable(tmp);
                }
            }
        }
        return fc;
    }
}
