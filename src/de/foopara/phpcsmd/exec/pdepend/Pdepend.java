package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.option.PdependOptions;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;
import java.io.File;
import java.util.HashSet;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class Pdepend {

    private boolean _enabled = true;

    private PdependReportTopComponent component = null;

    public boolean isEnabled() {
        return this._enabled;
    }

    public PdependResult run(FileObject file) {
        if (!GenericHelper.isDesirableFile(new File(PdependOptions.getScript()))
            || (!GenericHelper.isDesirableFile(file) && !GenericHelper.isDesirableFolder(file))
        ) {
            return this.setAndReturnDefault();
        }

        if(this.isEnabled() == false) {
            return this.setAndReturnDefault();
        }

        HashSet<File> tmpFiles = new HashSet<File>();


        StringBuilder cmd = new StringBuilder(PdependOptions.getScript());
        this.appendArgument(cmd, "--suffix=", "" + PdependOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude=", "" + PdependOptions.getExcludes());
        this.appendArgument(cmd, "--ignore=", "" + PdependOptions.getIgnores());
        this.appendArgument(cmd, "-d ", GenericHelper.implode(" -d ", PdependOptions.getIniOverwrite().split(";")));

        File summary = new File(System.getProperty("java.io.tmpdir"), "phpcsmd_pdepend_" + file.hashCode() + ".xml");
        this.appendArgument(cmd, "--summary-xml=", summary.getAbsolutePath());
        tmpFiles.add(summary);

        if (PdependOptions.getJDepend()) {
            File jdepend = new File(System.getProperty("java.io.tmpdir"), "phpcsmd_jdepend_" + file.hashCode() + ".xml");
            this.appendArgument(cmd, "--jdepend-xml=", jdepend.getAbsolutePath());
            tmpFiles.add(jdepend);
        }
        cmd.append(" ").append(GenericHelper.escapePath(file));
        Logger.getInstance().logPre(cmd.toString(), "pdepend command");

        PdependParser parser = new PdependParser();

        Object[] tmpFilesArray = tmpFiles.toArray();
        File[] outputFiles = new File[tmpFilesArray.length];
        for (int i=0;i<tmpFilesArray.length;i++) {
            outputFiles[i] = (File)tmpFilesArray[i];
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), outputFiles, this.component);

        for (File f : tmpFiles) {
            if (f.exists()) {
                f.delete();
            }
        }

        return parser.parse(reader);
    }

    public void setTopComponent(PdependReportTopComponent c) {
        this.component = c;
    }

    private PdependResult setAndReturnDefault() {

        return new PdependResult();
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(value);
        }
    }
}
