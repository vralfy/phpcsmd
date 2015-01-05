package de.foopara.phpcsmd.exec.phpmd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.option.PhpmdOptions;

/**
 *
 * @author nspecht
 */
public class Phpmd extends GenericExecute
{

    private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        Lookup lookup = GenericHelper.getFileLookup(file);
        if ((Boolean)PhpmdOptions.load(PhpmdOptions.Settings.ACTIVATED, lookup) == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File((String)PhpmdOptions.load(PhpmdOptions.Settings.SCRIPT, lookup)), lookup, false)
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if (this.isEnabled() == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        StringBuilder cmd = new StringBuilder(GenericExecute.escapePath((String)PhpmdOptions.load(PhpmdOptions.Settings.SCRIPT, lookup)));
        cmd.append(" ").append(GenericHelper.escapePath(file));
        cmd.append(" ").append("xml");
        cmd.append(" ").append((String)PhpmdOptions.load(PhpmdOptions.Settings.RULES, lookup));

        this.appendArgument(cmd, "--suffixes", (String)PhpmdOptions.load(PhpmdOptions.Settings.SUFFIXES, lookup));
        this.appendArgument(cmd, "--exclude", (String)PhpmdOptions.load(PhpmdOptions.Settings.EXCLUDE, lookup));
        this.appendArgument(cmd, "--minimumpriority", (String)PhpmdOptions.load(PhpmdOptions.Settings.MINPRIORITY, lookup));
        if ((Boolean)PhpmdOptions.load(PhpmdOptions.Settings.STRICT, lookup) == true) {
            cmd.append(" --strict");
        }
        Logger.getInstance().logPre(cmd.toString(), "pmd command");

        PhpmdXMLParser parser = new PhpmdXMLParser();
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, lookup);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        PhpmdResult res = parser.parse(reader[0], null);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        String staticFile = (String)PhpmdOptions.load(PhpmdOptions.Settings.STATIC, lookup);
        if (!staticFile.isEmpty()) {
            File staticFile2 = new File(staticFile);
            if (staticFile2.exists() && staticFile2.canRead()) {
                try {
                    Logger.getInstance().log("parsing " + staticFile + " for violoations in " + file.getPath(), "");
                    StringBuilder content = new StringBuilder();
                    content.append(new String(Files.readAllBytes(staticFile2.toPath())));
                    res.addAll(parser.parse(new GenericOutputReader(content), file.getPath()));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
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
