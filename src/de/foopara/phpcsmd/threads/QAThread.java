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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class QAThread extends Thread {

    private static ArrayList<QAThread> instances = new ArrayList<QAThread>();
    private FileObject fo = null;
    private boolean interupted = false;
    private boolean poke = true;

    private boolean enablePhpcs = false;

    private boolean enablePhpmd = false;

    private boolean enablePhpcpd = false;

    private Lookup lkp;

    public QAThread(Lookup lkp) {
        super();
        this.lkp = lkp;
        this.enablePhpcs = (Boolean)PhpcsOptions.load(PhpcsOptions.Settings.ACTIVATED, this.lkp);
        this.enablePhpmd = (Boolean)PhpmdOptions.load(PhpmdOptions.Settings.ACTIVATED, this.lkp);
        this.enablePhpcpd =(Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATED, this.lkp) || (Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, this.lkp);
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
        try {
            if (!GenericHelper.isDesirableFile(this.fo, this.lkp) || GenericHelper.isSymlink(FileUtil.toFile(this.fo))) {
                return;
            }

            for (QAThread t : QAThread.instances) {
                if (t.isThreadFor(this.fo)) {
                    t.interupt();
                }
                while (QAThread.instances.lastIndexOf(this) > 0) {
                }
            }

            QAThread.instances.add(this);
            if (!this.interupted && this.enablePhpcs) {
                new Phpcs(this.lkp).execute(this.fo);
            }
            if (!this.interupted && this.enablePhpmd) {
                new Phpmd(this.lkp).execute(this.fo);
            }
            if (!this.interupted && this.enablePhpcpd) {
                new Phpcpd(this.lkp).execute(this.fo);
            }


            if (!this.interupted) {
                GenericAnnotationBuilder.updateAnnotations(this.fo, this.lkp);
            }
            if (!this.interupted) {
                GenericNotification.displayNotification(this.fo, this.lkp);
            }
            if (!this.interupted) {
                ViolationRegistry.getInstance().reprintTasks(this.fo);
            }

            if (!this.interupted && this.poke) {
                GenericPokeRegistry.getInstance().poke(this.fo);
            }

            QAThread.instances.remove(this);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }
    }

    public void interupt() {
        this.interupted = true;
    }

    public boolean isInterupted() {
        return this.interupted;
    }
}
