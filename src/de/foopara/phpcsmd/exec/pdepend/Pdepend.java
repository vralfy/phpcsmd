/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.option.PdependOptions;
import java.io.File;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class Pdepend {
    private boolean _enabled = true;

    public boolean isEnabled() {
        return this._enabled;
    }

    public PdependResult run(FileObject file) {
        if (!GenericHelper.isDesirableFile(new File(PdependOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) return this.setAndReturnDefault(file);

        String tmpFile = System.getProperty("java.io.tmpdir") + "pdepend_" + file.hashCode() + ".xml";

        StringBuilder cmd = new StringBuilder(PdependOptions.getScript());
        this.appendArgument(cmd, "--suffixes=", "" + PdependOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude=", "" + PdependOptions.getExcludes());
        this.appendArgument(cmd, "--ignore=", "" + PdependOptions.getIgnores());

        this.appendArgument(cmd, "--summary-xml", tmpFile);

        cmd.append(" ").append(file.getPath());

        PdependParser parser = new PdependParser();
        GenericOutputReader reader = GenericProcess.run(cmd.toString());
        return parser.parse(reader);
    }

    private PdependResult setAndReturnDefault(FileObject file) {
        return new PdependResult();
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(value);
        }
    }
}
