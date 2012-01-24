/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.phpmd.PhpmdOptions;
import java.io.File;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
public class Phpmd extends GenericExecute {
   private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        PhpmdResult def = new PhpmdResult(null, null);

        if (!PhpmdOptions.getActivated()) return def;

        if (!GenericHelper.isDesirableFile(new File(PhpmdOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return def;
        }

        if(this.isEnabled() == false) return def;

        if (!iAmAlive()) return def;

        StringBuilder cmd = new StringBuilder(PhpmdOptions.getScript());
        cmd.append(" ").append(file.getPath());
        cmd.append(" ").append("xml");
        cmd.append(" ").append(PhpmdOptions.getRules());
        this.appendArgument(cmd, "--suffixes", PhpmdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude", PhpmdOptions.getExcludes());

        PhpmdXMLParser parser = new PhpmdXMLParser();
        if (!iAmAlive()) return def;
        GenericOutputReader reader = GenericProcess.run(cmd.toString());
        if (!iAmAlive()) return def;
        PhpmdResult res = parser.parse(reader);
        if (!iAmAlive()) return def;
        ViolationRegistry.getInstance().setPhpmd(file, res);
        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(" ").append(value);
        }
    }
}
