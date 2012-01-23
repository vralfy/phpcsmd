/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.exec.phpcs.Phpcs;
import de.foopara.phpcsmd.exec.phpmd.Phpmd;
import de.foopara.phpcsmd.option.GeneralOptions;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
abstract public class GenericExecute {
    public abstract boolean isEnabled();
    protected abstract GenericResult run(FileObject file, boolean annotations);

    protected GenericResult run(FileObject file) {
        return this.run(file, false);
    }

    public GenericResult execute(FileObject file) {
        return this.run(file, false);
    }

    public GenericResult execute(FileObject file, boolean annotations) {
        return this.run(file, annotations);
    }

    public static void executeQATools(FileObject fo) {
        QAThread qaThread = new QAThread();
        if (GeneralOptions.getThreaded()) {
            qaThread.start();
        } else {
            qaThread.run();
        }
    }

    private static class QAThread extends Thread {
        FileObject fo =null;

        public void QAThread(FileObject fo) {
            this.fo = fo;
        }

        @Override
        public void run() {
            new Phpcs().execute(this.fo);
            new Phpmd().execute(this.fo);
            ViolationRegistry.getInstance().reprintTasks(this.fo);
        }
    }
}
