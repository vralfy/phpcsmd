package de.foopara.phpcsmd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class DebugLog {

    private static DebugLog instance = null;
    private StringBuilder log = new StringBuilder();
    private static boolean enabled = false;

    public static DebugLog getInstance() {
        if (DebugLog.instance == null) {
            DebugLog.instance = new DebugLog();
        }
        return DebugLog.instance;
    }

    public static void put(String msg) {
        if (DebugLog.enabled) {
            DebugLog.getInstance().write(msg);
        }
    }

    public void write(String msg) {
        if (!DebugLog.enabled) {
            return;
        }
        System.out.println(msg);
        try {
            this.log.append(msg).append("\n");

            File f = new File("/home/nspecht/phpcsmd.log");
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(this.log.toString().getBytes());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
