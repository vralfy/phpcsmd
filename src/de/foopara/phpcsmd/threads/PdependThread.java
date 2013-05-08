package de.foopara.phpcsmd.threads;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.exec.pdepend.Pdepend;
import de.foopara.phpcsmd.exec.pdepend.PdependResult;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;

public class PdependThread extends Thread
{

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
        ProgressHandle handle = null;
        try {
            handle = ProgressHandleFactory.createHandle("phpcsmd pdepend scan", null, null);
            handle.start();
        } catch (NoClassDefFoundError e) {
            Logger.getInstance().log(e);
        } catch (Exception ex){
            Logger.getInstance().log(ex);
        }

        try {
            Pdepend pdepend = new Pdepend();
            pdepend.setTopComponent(this.component);
            PdependResult res = pdepend.run(fo);
            this.component.setPdependResult(res);
        } catch (Exception e) {
            Logger.getInstance().log(e);
        }

        if (handle != null) {
            handle.finish();
        }
    }

}
