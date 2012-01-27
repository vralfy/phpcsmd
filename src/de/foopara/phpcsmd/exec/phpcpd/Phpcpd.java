/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.phpcpd.PhpcpdOptions;
import java.io.File;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
public class Phpcpd extends GenericExecute {
   private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        PhpcpdResult def = new PhpcpdResult(null, null);

        if (!PhpcpdOptions.getActivated()) return def;

        if (!GenericHelper.isDesirableFile(new File(PhpcpdOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return def;
        }

        if(this.isEnabled() == false) return def;

        if (!iAmAlive()) return def;

        StringBuilder cmd = new StringBuilder(PhpcpdOptions.getScript());
        this.appendArgument(cmd, "--min-lines", "" + PhpcpdOptions.getMinLines());
        this.appendArgument(cmd, "--min-lines", "" + PhpcpdOptions.getMinTokens());
        this.appendArgument(cmd, "--suffixes", PhpcpdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude", PhpcpdOptions.getExcludes());
        cmd.append(" ").append(file.getPath());

        /*
        ExternalProcessBuilder epb = new ExternalProcessBuilder(PhpcsOptions.getScript());
        epb.workingDirectory(root);
        epb.addArgument("--standard=" + PhpcsOptions.getStandard());
        //epb.addArgument("...");
        epb.addArgument("--report=xml");
        epb.addArgument(file.getPath());
         */
        PhpcpdParser parser = new PhpcpdParser();
        if (!iAmAlive()) return def;
        GenericOutputReader reader = GenericProcess.run(cmd.toString());
        if (!iAmAlive()) return def;
        PhpcpdResult res = parser.parse(reader);
        if (!iAmAlive()) return def;
        ViolationRegistry.getInstance().setPhpcs(file, res);
        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(value);
        }
    }
}
