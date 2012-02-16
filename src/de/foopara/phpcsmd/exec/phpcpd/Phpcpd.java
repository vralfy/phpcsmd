/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpcpdOptions;
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
        if (!PhpcpdOptions.getActivated()) return this.setAndReturnDefault(file);

        if (!GenericHelper.isDesirableFile(new File(PhpcpdOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) return this.setAndReturnDefault(file);

        if (!iAmAlive()) return this.setAndReturnDefault(file);

        StringBuilder cmd = new StringBuilder(PhpcpdOptions.getScript());
        this.appendArgument(cmd, "--min-lines ", "" + PhpcpdOptions.getMinLines());
        this.appendArgument(cmd, "--min-tokens ", "" + PhpcpdOptions.getMinTokens());
        this.appendArgument(cmd, "--suffixes ", PhpcpdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude ", PhpcpdOptions.getExcludes());
        cmd.append(" ").append(file.getPath());

        PhpcpdParser parser = new PhpcpdParser();
        if (!iAmAlive()) return this.setAndReturnDefault(file);
        GenericOutputReader reader = GenericProcess.run(cmd.toString());
        if (!iAmAlive()) return this.setAndReturnDefault(file);
        PhpcpdResult res = parser.parse(reader);
        if (!iAmAlive()) return this.setAndReturnDefault(file);
        ViolationRegistry.getInstance().setPhpcpd(file, res);
        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(value);
        }
    }

    private GenericResult setAndReturnDefault(FileObject file) {
        GenericResult ret = new GenericResult(null, null, null);
        ViolationRegistry.getInstance().setPhpcpd(file, ret);
        return ret;
    }
}
