/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author n.specht
 */
public class RescanThread extends Thread {

    private FileObject fo = null;
    private ScanReportTopComponent component = null;

    public void setFileObject(FileObject fo) {
        this.fo = fo;
    }

    public void setTopComponent(ScanReportTopComponent c) {
        this.component = c;
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
        for (FileObject f2 : f.getChildren()) {
            if (f2.isData()) {
                fc += 1;
                QAThread qa = new QAThread();
                qa.setFileObject(f2);
                qa.run();
                this.component.setScannedFilecount(fc);
            } else if (f2.isFolder()) {
                fc = this.count(f2, fc);
            }
        }
        return fc;
    }
}
