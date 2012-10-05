package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.*;
import de.foopara.phpcsmd.option.PhpcpdOptions;
import java.io.File;
import java.util.HashMap;
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
        boolean run = false;
        if (PhpcpdOptions.getActivated()) {
            run = true;
        }

        if (PhpcpdOptions.getActivatedFolder() &&
                (ViolationRegistry.getInstance().getPhpcpdDependency(file).size() > 0
                || ViolationRegistry.getInstance().getPhpcpd(file).getSum() > 0)
        ) {
            run = true;
        }
        if (!run) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File(PhpcpdOptions.getScript()))
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if (this.isEnabled() == false) return this.setAndReturnCurrent(file);

        if (!iAmAlive()) return this.setAndReturnCurrent(file);


        StringBuilder cmd = this.getGenericCommand();
        cmd.append(" ").append(GenericHelper.getPhpcpdDistractor());
        cmd.append(" ").append(file.getPath());
        boolean updateDependencies = false;
        for (FileObject fo : ViolationRegistry.getInstance().getPhpcpdDependency(file)) {
            updateDependencies = true;
            cmd.append(" ").append(fo.getPath());
        }
        Logger.getInstance().log(cmd.toString());
        
        PhpcpdParser parser = new PhpcpdParser();
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);

        //ViolationRegistry.getInstance().flushPhpcpdDependency(file);
        PhpcpdResult res = parser.parse(reader[0], updateDependencies && false, file);
        if (!iAmAlive()) return this.setAndReturnCurrent(file);
        ViolationRegistry.getInstance().setPhpcpd(file, res);
        return res;
    }

    public HashMap<String, PhpcpdResult> runFolder(FileObject folder, boolean annotations) {
        if (!PhpcpdOptions.getActivatedFolder()) return new HashMap<String, PhpcpdResult>();

        if(this.isEnabled() == false) return new HashMap<String, PhpcpdResult>();

        if (!iAmAlive()) return new HashMap<String, PhpcpdResult>();

        StringBuilder cmd = this.getGenericCommand();
        cmd.append(" ").append(GenericHelper.getPhpcpdDistractor());
        cmd.append(" ").append(folder.getPath());

        PhpcpdFolderParser parser = new PhpcpdFolderParser();
        if (!iAmAlive()) return new HashMap<String, PhpcpdResult>();
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null);
        if (!iAmAlive()) return new HashMap<String, PhpcpdResult>();
        HashMap<String, PhpcpdResult> res = parser.parse(reader[0], folder);
        if (!iAmAlive()) return new HashMap<String, PhpcpdResult>();
        ViolationRegistry.getInstance().setPhpcpdFolder(res);
        return res;
    }

    private StringBuilder getGenericCommand() {
        StringBuilder cmd = new StringBuilder(PhpcpdOptions.getScript());
        this.appendArgument(cmd, "--min-lines ", "" + PhpcpdOptions.getMinLines());
        this.appendArgument(cmd, "--min-tokens ", "" + PhpcpdOptions.getMinTokens());
        this.appendArgument(cmd, "--suffixes ", PhpcpdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude ", PhpcpdOptions.getExcludes());
        return cmd;
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

    private GenericResult setAndReturnCurrent(FileObject file) {
        return ViolationRegistry.getInstance().getPhpcpd(file);
    }
}
