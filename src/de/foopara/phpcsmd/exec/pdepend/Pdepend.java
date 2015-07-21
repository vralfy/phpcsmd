package de.foopara.phpcsmd.exec.pdepend;

import java.io.File;
import java.util.HashSet;

import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.option.PdependOptions;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;

public class Pdepend
{

    private boolean _enabled = true;

    private PdependReportTopComponent component = null;

    public boolean isEnabled() {
        return this._enabled;
    }

    public PdependResult run(FileObject file) {
        Lookup lkp = GenericHelper.getFileLookup(file);

        if (!GenericHelper.isDesirableFile(new File((String)PdependOptions.load(PdependOptions.Settings.SCRIPT, lkp)), lkp, false)
                || (!GenericHelper.isDesirableFile(file)
                && !GenericHelper.isDesirableFolder(file))) {
            return this.setAndReturnDefault();
        }

        if (this.isEnabled() == false) {
            return this.setAndReturnDefault();
        }

        HashSet<File> tmpFiles = new HashSet<File>();


        StringBuilder cmd = new StringBuilder(GenericExecute.escapePath((String)PdependOptions.load(PdependOptions.Settings.SCRIPT, lkp)));
        this.appendArgument(cmd, "--suffix=", "" + (String)PdependOptions.load(PdependOptions.Settings.SUFFIXES, lkp));
        this.appendArgument(cmd, "--exclude=", "" + (String)PdependOptions.load(PdependOptions.Settings.EXCLUDE, lkp));
        this.appendArgument(cmd, "--ignore=", "" + (String)PdependOptions.load(PdependOptions.Settings.IGNORES, lkp));
        this.appendArgument(cmd, "-d ", GenericHelper.implode(
                " -d ",
                ((String)PdependOptions.load(PdependOptions.Settings.INIOVERWRITE, lkp)).split(";")));

        File summary = new File(System.getProperty("java.io.tmpdir"), "phpcsmd_pdepend_" + file.hashCode() + ".xml");
        this.appendArgument(cmd, "--summary-xml=", summary.getAbsolutePath());
        tmpFiles.add(summary);

        if ((Boolean)PdependOptions.load(PdependOptions.Settings.JDEPEND, lkp)) {
            File jdepend = new File(System.getProperty("java.io.tmpdir"), "phpcsmd_jdepend_" + file.hashCode() + ".xml");
            this.appendArgument(cmd, "--jdepend-xml=", jdepend.getAbsolutePath());
            tmpFiles.add(jdepend);
        }
        cmd.append(" ").append(GenericHelper.escapePath(file));
        Logger.getInstance().logPre(cmd.toString(), "pdepend command");

        PdependParser parser = new PdependParser();

        Object[] tmpFilesArray = tmpFiles.toArray();
        File[] outputFiles = new File[tmpFilesArray.length];
        for (int i = 0; i < tmpFilesArray.length; i++) {
            outputFiles[i] = (File)tmpFilesArray[i];
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), outputFiles, this.component, lkp);
        if (reader.length < 1) {
            Logger.getInstance().logPre("no output from commmand line", "pdepend command");
            return this.setAndReturnDefault();
        }

        for (File f : tmpFiles) {
            if (f.exists()) {
                boolean delete = f.delete();
                if (!delete) {
                    Logger.getInstance().logPre("failed to delete " + f.getAbsolutePath(), "pdepend");
                }
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
