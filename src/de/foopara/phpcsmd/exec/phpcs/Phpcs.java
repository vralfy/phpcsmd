package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpcsOptions;
import java.io.File;
import java.io.InputStream;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class Phpcs extends GenericExecute
{

    private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        Lookup lookup = GenericHelper.getFileLookup(file);
        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.ACTIVATED, lookup) == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File((String)PhpcsOptions.load(PhpcsOptions.Settings.SCRIPT, lookup)), lookup)
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if (this.isEnabled() == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        CustomStandard cstandard = null;
        StringBuilder cmd = new StringBuilder((String)PhpcsOptions.load(PhpcsOptions.Settings.SCRIPT, lookup));
        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.EXTRAS, lookup) == true
                || ((String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, lookup)).trim().length() == 0) {
            cstandard = new CustomStandard(lookup);
            this.appendArgument(cmd, "--standard=", cstandard.toString());
        } else {
            this.appendArgument(cmd, "--standard=", (String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, lookup));
        }
        this.appendArgument(cmd, "--sniffs=", (String)PhpcsOptions.load(PhpcsOptions.Settings.SNIFFS, lookup));
        this.appendArgument(cmd, "--extensions=", (String)PhpcsOptions.load(PhpcsOptions.Settings.EXTENSIONS, lookup));
        this.appendArgument(cmd, "--ignore=", (String)PhpcsOptions.load(PhpcsOptions.Settings.IGNORES, lookup));
        this.appendArgument(cmd, "-d ", GenericHelper.implode(" -d ", ((String)PhpcsOptions.load(PhpcsOptions.Settings.INIOVERWRITE, lookup)).split(";")));

        if ((Integer)PhpcsOptions.load(PhpcsOptions.Settings.TABWIDTH, lookup) > -1) {
            cmd.append(" --tab-width=").append((Integer)PhpcsOptions.load(PhpcsOptions.Settings.TABWIDTH, lookup));
        }

        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.WARNINGS, lookup) == true) {
            cmd.append(" -w");
        } else {
            cmd.append(" -n");
        }

        cmd.append(" --report=xml");

        cmd.append(" ").append(GenericHelper.escapePath(file));
        Logger.getInstance().logPre(cmd.toString(), "phpcs command");

        /*
         * ExternalProcessBuilder epb = new
         * ExternalProcessBuilder(PhpcsOptions.getScript());
         * epb.workingDirectory(root); epb.addArgument("--standard=" +
         * PhpcsOptions.getStandard()); //epb.addArgument("...");
         * epb.addArgument("--report=xml"); epb.addArgument(file.getPath());
         */
        PhpcsXMLParser parser = new PhpcsXMLParser(lookup);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, lookup);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        PhpcsResult res = parser.parse(reader[0]);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        ViolationRegistry.getInstance().setPhpcs(file, res);

        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.EXTRAS, lookup) && cstandard != null) {
            cstandard.delete();
        }

        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(value);
        }
    }

    private GenericResult setAndReturnDefault(FileObject file) {
        GenericResult ret = new GenericResult(null, null, null);
        ViolationRegistry.getInstance().setPhpcs(file, ret);
        return ret;
    }

    private GenericResult setAndReturnCurrent(FileObject file) {
        return ViolationRegistry.getInstance().getPhpcs(file);
    }

    public static String[] getStandards(String executable) {
        try {
            File script = new File(executable);
            if (!script.exists() || !script.canExecute() || !script.isFile()) {
                return new String[0];
            }
            Process child = Runtime.getRuntime().exec(executable + " -i");
            InputStream in = child.getInputStream();
            StringBuilder tmp = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                tmp.append((char)c);
            }
            String installed[] = tmp.toString()
                    .replaceFirst("The installed.*are ", "")
                    .replaceFirst(" and ", ", ")
                    .split(", ");
            return installed;
        } catch (Exception e) {
            Logger.getInstance().log(e);
        }

        return new String[0];
    }

}
