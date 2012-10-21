package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpmdOptions;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class Phpmd extends GenericExecute {
    private boolean _enabled = true;
    private Lookup lkp;

    public Phpmd(Lookup lkp) {
        this.lkp = lkp;
    }

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        if ((Boolean)PhpmdOptions.load(PhpmdOptions.Settings.ACTIVATED, this.lkp) == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File((String)PhpmdOptions.load(PhpmdOptions.Settings.SCRIPT, this.lkp)), this.lkp)
                || !GenericHelper.isDesirableFile(file, this.lkp)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        StringBuilder cmd = new StringBuilder((String)PhpmdOptions.load(PhpmdOptions.Settings.SCRIPT, this.lkp));
        cmd.append(" ").append(GenericHelper.escapePath(file));
        cmd.append(" ").append("xml");
        cmd.append(" ").append((String)PhpmdOptions.load(PhpmdOptions.Settings.RULES, this.lkp));

        this.appendArgument(cmd, "--suffixes", (String)PhpmdOptions.load(PhpmdOptions.Settings.SUFFIXES, this.lkp));
        this.appendArgument(cmd, "--exclude", (String)PhpmdOptions.load(PhpmdOptions.Settings.EXCLUDE, this.lkp));
        this.appendArgument(cmd, "--minimumpriority", (String)PhpmdOptions.load(PhpmdOptions.Settings.MINPRIORITY, this.lkp));
        if ((Boolean)PhpmdOptions.load(PhpmdOptions.Settings.STRICT, this.lkp) == true) {
            cmd.append(" --strict");
        }
        Logger.getInstance().logPre(cmd.toString(), "pmd command");

        PhpmdXMLParser parser = new PhpmdXMLParser();
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, this.lkp);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        PhpmdResult res = parser.parse(reader[0]);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
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
