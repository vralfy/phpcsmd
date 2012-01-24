/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.option.GeneralOptions;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
abstract public class GenericExecute {

    protected QAThread qaThread = null;
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

    public GenericExecute setThread(QAThread thread) {
        this.qaThread = thread;
        return this;
    }

    protected boolean iAmAlive() {
        if (this.qaThread == null) return true;
        return !this.qaThread.isInterupted();
    }

    public static void executeQATools(FileObject fo) {
        QAThread qaThread = new QAThread();
        qaThread.setFileObject(fo);
        if (GeneralOptions.getThreaded()) {
            qaThread.start();
        } else {
            qaThread.qarun();
        }
    }
}