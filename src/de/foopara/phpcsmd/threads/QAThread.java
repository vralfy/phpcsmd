/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.threads;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.exec.phpcpd.Phpcpd;
import de.foopara.phpcsmd.exec.phpcs.Phpcs;
import de.foopara.phpcsmd.exec.phpmd.Phpmd;
import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericNotification;
import de.foopara.phpcsmd.generics.GenericPokeRegistry;
import java.util.ArrayList;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class QAThread extends Thread {

    private static ArrayList<QAThread> instances = new ArrayList<QAThread>();
    private FileObject fo = null;
    private boolean interupted = false;
    private boolean poke = true;

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
        this.qarun();
    }

    public void qarun() {
        if (!GenericHelper.isDesirableFile(this.fo)) {
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
        if (!this.interupted) {
            new Phpcs().execute(this.fo);
        }
        if (!this.interupted) {
            new Phpmd().execute(this.fo);
        }
        if (!this.interupted) {
            new Phpcpd().execute(this.fo);
        }


        if (!this.interupted) {
            GenericAnnotationBuilder.updateAnnotations(this.fo);
        }
        if (!this.interupted) {
            GenericNotification.displayNotification(this.fo);
        }
        if (!this.interupted) {
            ViolationRegistry.getInstance().reprintTasks(this.fo);
        }

        if (!this.interupted && this.poke) {
            GenericPokeRegistry.getInstance().poke(this.fo);
        }

        QAThread.instances.remove(this);
    }

    public void interupt() {
        this.interupted = true;
    }

    public boolean isInterupted() {
        return this.interupted;
    }
}
