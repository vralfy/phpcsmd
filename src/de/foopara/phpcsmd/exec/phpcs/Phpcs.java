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
public class Phpcs extends GenericExecute {
    private boolean _enabled = true;

    public Phpcs(Lookup lkp) {
        this.lkp = lkp;
    }

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.ACTIVATED, this.lkp) == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File((String)PhpcsOptions.load(PhpcsOptions.Settings.SCRIPT, this.lkp)), this.lkp)
                || !GenericHelper.isDesirableFile(file, this.lkp)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) return this.setAndReturnCurrent(file);

        if (!iAmAlive()) return this.setAndReturnCurrent(file);

        CustomStandard cstandard = null;
        StringBuilder cmd = new StringBuilder((String)PhpcsOptions.load(PhpcsOptions.Settings.SCRIPT, this.lkp));
        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.EXTRAS, this.lkp) == true
            || ((String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, this.lkp)).trim().length() == 0
        ) {
            cstandard = new CustomStandard();
            this.appendArgument(cmd, "--standard=", cstandard.toString());
        } else {
            this.appendArgument(cmd, "--standard=", (String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, this.lkp));
        }
        this.appendArgument(cmd, "--sniffs=", (String)PhpcsOptions.load(PhpcsOptions.Settings.SNIFFS, this.lkp));
        this.appendArgument(cmd, "--extensions=", (String)PhpcsOptions.load(PhpcsOptions.Settings.EXTENSIONS, this.lkp));
        this.appendArgument(cmd, "--ignore=", (String)PhpcsOptions.load(PhpcsOptions.Settings.IGNORES, this.lkp));
        this.appendArgument(cmd, "-d ", GenericHelper.implode(" -d ", ((String)PhpcsOptions.load(PhpcsOptions.Settings.INIOVERWRITE, this.lkp)).split(";")));

        if ((Integer)PhpcsOptions.load(PhpcsOptions.Settings.TABWIDTH, this.lkp) > -1) {
            cmd.append(" --tab-width=").append((String)PhpcsOptions.load(PhpcsOptions.Settings.TABWIDTH, this.lkp));
        }

        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.WARNINGS, this.lkp) == true) {
            cmd.append(" -w");
        } else {
            cmd.append(" -n");
        }

        cmd.append(" --report=xml");

        cmd.append(" ").append(GenericHelper.escapePath(file));
        Logger.getInstance().logPre(cmd.toString(),"phpcs command");

        /*
        ExternalProcessBuilder epb = new ExternalProcessBuilder(PhpcsOptions.getScript());
        epb.workingDirectory(root);
        epb.addArgument("--standard=" + PhpcsOptions.getStandard());
        //epb.addArgument("...");
        epb.addArgument("--report=xml");
        epb.addArgument(file.getPath());
         */
        PhpcsXMLParser parser = new PhpcsXMLParser(this.lkp);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, this.lkp);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        PhpcsResult res = parser.parse(reader[0]);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        ViolationRegistry.getInstance().setPhpcs(file, res);

        if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.EXTRAS, this.lkp) && cstandard != null) {
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
                return null;
            }
            Process child = Runtime.getRuntime().exec(executable + " -i");
            InputStream in = child.getInputStream();
            StringBuilder tmp = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                tmp.append((char) c);
            }
            String installed[] = tmp.toString()
                    .replaceFirst("The installed.*are ", "")
                    .replaceFirst(" and ", ", ")
                    .split(", ");
            return installed;
        } catch (Exception e) {
            Logger.getInstance().log(e);
        }

        return null;
    }
}
