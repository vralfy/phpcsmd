package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.exec.pdepend.Pdepend;
import de.foopara.phpcsmd.exec.pdepend.PdependResult;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class PdependThread extends Thread {

    private FileObject fo = null;
    private PdependReportTopComponent component = null;

    public void setFileObject(FileObject fo) {
        this.fo = fo;
    }

    public void setTopComponent(PdependReportTopComponent c) {
        this.component = c;
    }


    /*
     * Nur damit Netbeans die Klappe hällt
     */
    @Override
    public void run() {
        Pdepend pdepend = new Pdepend();
        pdepend.setTopComponent(this.component);
        PdependResult res = pdepend.run(fo);
        this.component.setPdependResult(res);
    }
}
