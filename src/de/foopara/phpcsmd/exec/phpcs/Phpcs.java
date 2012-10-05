package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpcsOptions;
import java.io.File;
import org.openide.filesystems.FileObject;

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
        if (!PhpcsOptions.getActivated()) return this.setAndReturnCurrent(file);

        if (!GenericHelper.isDesirableFile(new File(PhpcsOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if(this.isEnabled() == false) return this.setAndReturnCurrent(file);

        if (!iAmAlive()) return this.setAndReturnCurrent(file);

        CustomStandard cstandard = null;
        StringBuilder cmd = new StringBuilder(PhpcsOptions.getScript());
        if (PhpcsOptions.getExtras() || PhpcsOptions.getStandard().trim().length() == 0) {
            cstandard = new CustomStandard();
            this.appendArgument(cmd, "--standard=", cstandard.toString());
        } else {
            this.appendArgument(cmd, "--standard=", PhpcsOptions.getStandard());
        }
        this.appendArgument(cmd, "--sniffs=", PhpcsOptions.getSniffs());
        this.appendArgument(cmd, "--extensions=", PhpcsOptions.getExtensions());
        this.appendArgument(cmd, "--ignore=", PhpcsOptions.getIgnore());
        this.appendArgument(cmd, "-d ", GenericHelper.implode(" -d ", PhpcsOptions.getIniOverwrite().split(";")));

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
        Logger.getInstance().log(cmd.toString());
        
        /*
        ExternalProcessBuilder epb = new ExternalProcessBuilder(PhpcsOptions.getScript());
        epb.workingDirectory(root);
        epb.addArgument("--standard=" + PhpcsOptions.getStandard());
        //epb.addArgument("...");
        epb.addArgument("--report=xml");
        epb.addArgument(file.getPath());
         */
        PhpcsXMLParser parser = new PhpcsXMLParser();
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        PhpcsResult res = parser.parse(reader[0]);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        ViolationRegistry.getInstance().setPhpcs(file, res);

        if (PhpcsOptions.getExtras() && cstandard != null) {
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
}
