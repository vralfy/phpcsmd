package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author n.specht
 */
public class FileCountThread extends Thread {

    private FileObject fo = null;
    private boolean interupted = false;
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
        this.count();
    }

    public void count() {
        File f = FileUtil.toFile(this.fo);
        this.count(f, 0);

    }

    private int count(File f, int fc) {
        for (File f2 : f.listFiles()) {
            if (GenericHelper.isDesirableFile(f2)) {
                fc += 1;
                this.component.setMaximumFilecount(fc);
            } else if (f2.isDirectory()) {
                fc = this.count(f2, fc);
            }
        }
        return fc;
    }
}
