package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpmdOptions;
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
        if (!PhpmdOptions.getActivated()) return this.setAndReturnCurrent(file);

        if (!GenericHelper.isDesirableFile(new File(PhpmdOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) return this.setAndReturnCurrent(file);

        if (!iAmAlive()) return this.setAndReturnCurrent(file);

        StringBuilder cmd = new StringBuilder(PhpmdOptions.getScript());
        cmd.append(" ").append(GenericHelper.escapePath(file));
        cmd.append(" ").append("xml");
        cmd.append(" ").append(PhpmdOptions.getRules());

        this.appendArgument(cmd, "--suffixes", PhpmdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude", PhpmdOptions.getExcludes());
        this.appendArgument(cmd, "--minimumpriority", PhpmdOptions.getMinPriority());
        if (PhpmdOptions.getStrict()) {
            cmd.append(" --strict");
        }
        Logger.getInstance().logPre(cmd.toString(), "pmd command");

        PhpmdXMLParser parser = new PhpmdXMLParser();
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        PhpmdResult res = parser.parse(reader[0]);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        ViolationRegistry.getInstance().setPhpmd(file, res);
        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(" ").append(value);
        }
    }

    private GenericResult setAndReturnDefault(FileObject file) {
        GenericResult ret = new GenericResult(null, null, null);
        ViolationRegistry.getInstance().setPhpmd(file, ret);
        return ret;
    }

    private GenericResult setAndReturnCurrent(FileObject file) {
        return ViolationRegistry.getInstance().getPhpmd(file);
    }
}
