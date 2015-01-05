package de.foopara.phpcsmd.generics;

import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

import de.foopara.phpcsmd.option.GeneralOptions;
import de.foopara.phpcsmd.threads.QAThread;

/**
 *
 * @author nspecht
 */
abstract public class GenericExecute
{

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
        if (this.qaThread == null) {
            return true;
        }
        return !this.qaThread.isInterupted();
    }

    public static void executeQATools(FileObject fo) {
        Lookup lkp = GenericHelper.getFileLookup(fo);
        QAThread qaThread = new QAThread();
        qaThread.setFileObject(fo);
        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.THREADED, lkp)) {
            qaThread.start();
        } else {
            qaThread.qarun();
        }
    }

    public static String escapePath(String executable) {
      String os = System.getProperty("os.name").toLowerCase();
      if (os.indexOf("win") >= 0) {
        return "\"" + executable + "\"";
      }

      return executable.replace(" ", "\\ ");
    }
}