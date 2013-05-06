package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.exec.phpcpd.Phpcpd;
import de.foopara.phpcsmd.exec.phpcs.Phpcs;
import de.foopara.phpcsmd.exec.phpmd.Phpmd;
import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericNotification;
import de.foopara.phpcsmd.generics.GenericPokeRegistry;
import de.foopara.phpcsmd.option.PhpcpdOptions;
import de.foopara.phpcsmd.option.PhpcsOptions;
import de.foopara.phpcsmd.option.PhpmdOptions;
import java.io.IOException;
import java.util.ArrayList;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class QAThread extends Thread
{

    private static final ArrayList<QAThread> instances = new ArrayList<QAThread>();

    private static final String logCaption = "phpcsmd thread";

    private FileObject fo = null;

    private boolean interupted = false;

    private boolean poke = true;

    private boolean enablePhpcs = false;

    private boolean enablePhpmd = false;

    private boolean enablePhpcpd = false;

    private boolean enableNotification = true;

    public QAThread() {
        super();
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

    public void enableNotification(boolean enable) {
        this.enableNotification = enable;
    }

    public void setFileObject(FileObject fo) {
        this.fo = fo;
        Lookup lkp = GenericHelper.getFileLookup(fo);
        this.enablePhpcs = (Boolean)PhpcsOptions.load(PhpcsOptions.Settings.ACTIVATED, lkp);
        this.enablePhpmd = (Boolean)PhpmdOptions.load(PhpmdOptions.Settings.ACTIVATED, lkp);
        this.enablePhpcpd = (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATED, lkp) || (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, lkp);
    }

    public void setPoking(boolean poke) {
        this.poke = poke;
    }

    public boolean isThreadFor(FileObject fo) {
        return fo.getPath().compareTo(fo.getPath()) == 0;
    }

    /*
     * Nur damit Netbeans die Klappe hällt
     */
    @Override
    public void run() {
//        try {
//            this.join(GeneralOptions.getTimeout());
//        } catch (InterruptedException ex) {
//            Logger.getInstance().log(ex);
//            Exceptions.printStackTrace(ex);
//        }
        this.qarun();
    }

    public void qarun() {
        ProgressHandle handle = null;
        try {
            handle = ProgressHandleFactory.createHandle("phpcsmd", null, null);
        } catch (NoClassDefFoundError e) {
            Logger.getInstance().log(e);
        } catch (Exception ex) {
            Logger.getInstance().log(ex);
        }

        try {
            if (!GenericHelper.isDesirableFile(this.fo) || GenericHelper.isSymlink(FileUtil.toFile(this.fo))) {
                if (handle != null) {
                    handle.finish();
                }
                return;
            }

            for (QAThread t : QAThread.instances) {
                if (t.isThreadFor(this.fo)) {
                    t.interuptWork();
                }
                while (QAThread.instances.lastIndexOf(this) > 0) {
                }
            }

            int tasks = 4
                    + (this.enablePhpcs ? 1 : 0)
                    + (this.enablePhpmd ? 1 : 0)
                    + (this.enablePhpcpd ? 1 : 0);
            int currentTask = 0;
            if (handle != null) {
                handle.start(tasks);
            }
            Logger.getInstance().logPre("task count: " + tasks, "Starting phpcsmd thread");

            QAThread.instances.add(this);
            if (!this.interupted && this.enablePhpcs) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("running phpcs", currentTask);
                }
                new Phpcs().execute(this.fo);
            }
            if (!this.interupted && this.enablePhpmd) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("running phpmd", currentTask);
                }
                new Phpmd().execute(this.fo);
            }
            if (!this.interupted && this.enablePhpcpd) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("running phpcpd", currentTask);
                }
                new Phpcpd().execute(this.fo);
            }

            if (!this.interupted) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("updating annotations", currentTask);
                }
                GenericAnnotationBuilder.updateAnnotations(this.fo);
            }
            if (!this.interupted) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("display notification", currentTask);
                }
                if (this.enableNotification) {
                    GenericNotification.displayNotification(this.fo);
                }
            }
            if (!this.interupted) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("updating action items", currentTask);
                }
                ViolationRegistry.getInstance().reprintTasks(this.fo);
            }

            if (!this.interupted && this.poke) {
                currentTask++;
                Logger.getInstance().logPre("task: " + currentTask + " of " + tasks, QAThread.logCaption);
                if (handle != null) {
                    handle.progress("updating related scan reports", currentTask);
                }
                GenericPokeRegistry.getInstance().poke(this.fo);
            }

            QAThread.instances.remove(this);
            Logger.getInstance().logPre("finished successful", QAThread.logCaption);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        }

        if (handle != null) {
            handle.finish();
        }
    }

    public void interuptWork() {
        this.interupted = true;
    }

    public boolean isInterupted() {
        return this.interupted;
    }

}
