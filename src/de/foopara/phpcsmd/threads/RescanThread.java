package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.exec.phpcpd.Phpcpd;
import de.foopara.phpcsmd.exec.phpcpd.PhpcpdResult;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.option.PhpcpdOptions;
import de.foopara.phpcsmd.option.PhpcsOptions;
import de.foopara.phpcsmd.option.PhpmdOptions;
import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class RescanThread extends Thread
{

    private FileObject fo = null;

    private ScanReportTopComponent component = null;

    private boolean retrieveValuesFromRegistry = false;

    private boolean enablePhpcs = false;

    private boolean enablePhpmd = false;

    private boolean enablePhpcpd = false;

    private Lookup lkp;

    private boolean interupted = false;

    private ProgressHandle handle = null;

    public RescanThread(Lookup lkp) {
        super();
        this.lkp = lkp;
        this.enablePhpcs = (Boolean)PhpcsOptions.load(PhpcsOptions.Settings.ACTIVATED, this.lkp);
        this.enablePhpmd = (Boolean)PhpmdOptions.load(PhpmdOptions.Settings.ACTIVATED, this.lkp);
        this.enablePhpcpd = (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATED, this.lkp) || (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, this.lkp);
    }

    public void enablePhpcs(boolean enable) {
        this.enablePhpcs = enable;
    }

    public void enablePhpmd(boolean enable) {
        this.enablePhpmd = enable;
    }

    public void enablePhpcpd(boolean enable) {
        this.enablePhpcpd = enable;
    }

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
        this.count(this.fo, -1);
        this.component.setRescanDone();
        Logger.getInstance().logPre("finished", "RescanThread", Logger.Severity.USELESS);
    }

    private int count(FileObject f, int fc) {
        Logger.getInstance().logPre("starting " + f.getPath(), "RescanThread", Logger.Severity.USELESS);
        boolean firstRun = (fc == -1);
        if (fc < 0) {
            fc = 0;
        }

        if (firstRun) {
            try {
                this.handle = ProgressHandleFactory.createHandle("phpcsmd folder scan", null, null);
                this.handle.start();
            } catch (NoClassDefFoundError e) {
                if (this.handle != null) {
                    this.handle.finish();
                }
                Logger.getInstance().log(e);
            } catch (Exception e) {
                if (this.handle != null) {
                    this.handle.finish();
                }
                Logger.getInstance().log(e);
            }
        }

        if (this.doInterupt(this.handle)) {
            return fc;
        }

        for (FileObject f2 : f.getChildren()) {
            if (this.handle != null) {
                this.handle.progress(f2.getPath());
            }
            Logger.getInstance().logPre(f2.getPath(), "RescanThread", Logger.Severity.USELESS);
            if (this.doInterupt(handle)) {
                return fc;
            }
            try {
                if (GenericHelper.isDesirableFile(f2) && !GenericHelper.isSymlink(FileUtil.toFile(f2))) {
                    if (this.handle != null) {
                        handle.progress("file scan: " + f2.getPath());
                    }
                    Logger.getInstance().logPre("file scan " + f2.getPath(), "RescanThread", Logger.Severity.USELESS);
                    fc += 1;
                    if (!this.retrieveValuesFromRegistry) {
                        QAThread qa = new QAThread(this.lkp);
                        qa.enableNotification(false);
                        qa.enablePhpcs(this.enablePhpcs);
                        qa.enablePhpmd(this.enablePhpmd);
                        qa.enablePhpcpd(this.enablePhpcpd);
                        if ((Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, this.lkp)) {
                            qa.enablePhpcpd(false);
                        }
                        this.component.setCurrentScannedFile(f2);
                        qa.setFileObject(f2);
                        qa.setPoking(false);
                        qa.run();
                    }
                    this.component.setScannedFilecount(fc);
                    this.component.addElementToTable(f2);
                    Logger.getInstance().logPre("file scan " + f2.getPath() + " finished", "RescanThread", Logger.Severity.USELESS);
                } else if (f2.isFolder() && !GenericHelper.isSymlink(FileUtil.toFile(f2))) {
                    if (this.handle != null) {
                        handle.progress("folder: " + f2.getPath());
                    }
                    Logger.getInstance().logPre("folder scan " + f2.getPath(), "RescanThread", Logger.Severity.USELESS);
                    fc = this.count(f2, fc);
                    Logger.getInstance().logPre("folder scan " + f2.getPath() + " finished", "RescanThread", Logger.Severity.USELESS);
                }

            } catch (IOException ex) {
                Logger.getInstance().log(ex);
                Exceptions.printStackTrace(ex);
            }
        }
        if (this.doInterupt(handle)) {
            return fc;
        }
        Logger.getInstance().logPre("finished first part " + f.getPath(), "RescanThread", Logger.Severity.USELESS);

        if (GenericHelper.isDesirableFolder(f)
                && firstRun
                && this.enablePhpcpd
                && (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, this.lkp)
                ) {
            if (this.handle != null) {
                this.handle.progress("phpcpd folder scan");
            }
            Phpcpd cpdTask = new Phpcpd();
            HashMap<String, PhpcpdResult> res = cpdTask.runFolder(f, true);
            Logger.getInstance().logPre("cpdTask " + f.getPath() + " finished", "RescanThread", Logger.Severity.USELESS);
            for (String path : res.keySet()) {
                FileObject tmp = FileUtil.toFileObject(new File(path));
                if (tmp != null) {
                    this.component.addElementToTable(tmp);
                }
            }
        }

        if (this.handle != null) {
            this.handle.finish();
        }
        Logger.getInstance().logPre("finished " + f.getPath(), "RescanThread", Logger.Severity.USELESS);
        return fc;
    }

    public void interuptWork() {
        this.interupted = true;
    }

    private boolean doInterupt(ProgressHandle handle) {
        if (this.interupted && handle != null) {
            handle.finish();
            this.component.setRescanDone();
        }

        return this.interupted;
    }
}
