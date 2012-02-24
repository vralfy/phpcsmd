/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.option.PdependOptions;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;
import java.io.File;
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

        File tmpFile = new File(System.getProperty("java.io.tmpdir"), "pdepend_" + file.hashCode() + ".xml");

        StringBuilder cmd = new StringBuilder(PdependOptions.getScript());
        this.appendArgument(cmd, "--suffix=", "" + PdependOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude=", "" + PdependOptions.getExcludes());
        this.appendArgument(cmd, "--ignore=", "" + PdependOptions.getIgnores());

        this.appendArgument(cmd, "--summary-xml=", tmpFile.getAbsolutePath());

        cmd.append(" ").append(file.getPath());

        PdependParser parser = new PdependParser();
        GenericOutputReader reader = GenericProcess.run(cmd.toString(), tmpFile, this.component);
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
