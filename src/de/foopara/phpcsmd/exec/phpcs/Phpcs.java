/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.phpcs.PhpcsOptions;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author nspecht
 */
public class Phpcs extends GenericExecute {
   private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        PhpcsResult def = new PhpcsResult(null, null);

        if (!PhpcsOptions.getActivated()) return def;

        if (!GenericHelper.isDesirableFile(new File(PhpcsOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return def;
        }

        if(this.isEnabled() == false) return def;

        if (!iAmAlive()) return def;

        StringBuilder cmd = new StringBuilder(PhpcsOptions.getScript());
        this.appendArgument(cmd, "--standard=", PhpcsOptions.getStandard());
        this.appendArgument(cmd, "--sniffs=", PhpcsOptions.getSniffs());
        this.appendArgument(cmd, "--extensions=", PhpcsOptions.getExtensions());
        this.appendArgument(cmd, "--ignore=", PhpcsOptions.getIgnore());

        if (PhpcsOptions.getTabwidth() > -1) {
            cmd.append(" --tab-width=").append(PhpcsOptions.getTabwidth());
        }

        if (PhpcsOptions.getWarnings()) {
            cmd.append(" -w");
        } else {
            cmd.append(" -n");
        }

        cmd.append(" --report=xml");
        cmd.append(" ").append(file.getPath());

        /*
        ExternalProcessBuilder epb = new ExternalProcessBuilder(PhpcsOptions.getScript());
        epb.workingDirectory(root);
        epb.addArgument("--standard=" + PhpcsOptions.getStandard());
        //epb.addArgument("...");
        epb.addArgument("--report=xml");
        epb.addArgument(file.getPath());
         */
        PhpcsXMLParser parser = new PhpcsXMLParser();
        if (!iAmAlive()) return def;
        GenericOutputReader reader = GenericProcess.run(cmd.toString());
        if (!iAmAlive()) return def;
        PhpcsResult res = parser.parse(reader);
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
