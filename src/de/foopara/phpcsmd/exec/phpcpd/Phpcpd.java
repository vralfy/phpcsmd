package de.foopara.phpcsmd.exec.phpcpd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

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
import de.foopara.phpcsmd.option.PhpcpdOptions;

/**
 *
 * @author nspecht
 */
public class Phpcpd extends GenericExecute
{

    private boolean _enabled = true;

    @Override
    public boolean isEnabled() {
        return this._enabled;
    }

    @Override
    protected GenericResult run(FileObject file, boolean annotations) {
        Lookup lookup = GenericHelper.getFileLookup(file);
        boolean run = false;
        if ((Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATED, lookup)) {
            run = true;
        }

        if ((Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, lookup)
                && (ViolationRegistry.getInstance().getPhpcpdDependency(file).size() > 0
                || ViolationRegistry.getInstance().getPhpcpd(file).getSum() > 0)) {
            run = true;
        }
        if (!run) {
            return this.setAndReturnCurrent(file);
        }

        if (!GenericHelper.isDesirableFile(new File((String)PhpcpdOptions.load(PhpcpdOptions.Settings.SCRIPT, lookup)), lookup, false)
                || !GenericHelper.isDesirableFile(file)) {
            return this.setAndReturnDefault(file);
        }

        if (this.isEnabled() == false) {
            return this.setAndReturnCurrent(file);
        }

        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }


        StringBuilder cmd = this.getGenericCommand(lookup);
        cmd.append(" ").append(GenericHelper.getPhpcpdDistractor());
        cmd.append(" ").append(GenericHelper.escapePath(file));

        boolean updateDependencies = false;
        for (FileObject fo : ViolationRegistry.getInstance().getPhpcpdDependency(file)) {
            updateDependencies = true;
            cmd.append(" ").append(fo.getPath());
        }
        Logger.getInstance().logPre(cmd.toString(), "php-cpd command");

        PhpcpdParser parser = new PhpcpdParser();
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, lookup);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        //ViolationRegistry.getInstance().flushPhpcpdDependency(file);
        PhpcpdResult res = parser.parse(reader[0], updateDependencies, file);
        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        String staticFile = (String)PhpcpdOptions.load(PhpcpdOptions.Settings.STATIC, lookup);
        if (!staticFile.isEmpty()) {
            File staticFile2 = new File(staticFile);
            if (staticFile2.exists() && staticFile2.canRead()) {
                try {
                    Logger.getInstance().log("parsing " + staticFile + " for violoations in " + file.getPath(), "");
                    StringBuilder content = new StringBuilder();
                    content.append(new String(Files.readAllBytes(staticFile2.toPath())));
                    res.addAll(parser.parse(new GenericOutputReader(content), updateDependencies, file));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        if (!iAmAlive()) {
            return this.setAndReturnCurrent(file);
        }

        ViolationRegistry.getInstance().setPhpcpd(file, res);
        return res;
    }

    public HashMap<String, PhpcpdResult> runFolder(FileObject folder, boolean annotations) {
        Lookup lookup = GenericHelper.getFileLookup(folder);
        if ((Boolean)PhpcpdOptions.load(PhpcpdOptions.Settings.ACTIVATEDFOLDER, lookup) == false) {
            return new HashMap<String, PhpcpdResult>();
        }

        if (this.isEnabled() == false) {
            return new HashMap<String, PhpcpdResult>();
        }

        if (!iAmAlive()) {
            return new HashMap<String, PhpcpdResult>();
        }

        StringBuilder cmd = this.getGenericCommand(lookup);
        cmd.append(" ").append(GenericHelper.getPhpcpdDistractor());
        cmd.append(" ").append(GenericHelper.escapePath(folder));
        Logger.getInstance().logPre(cmd.toString(), "php-cpd command (folder scan)");

        PhpcpdFolderParser parser = new PhpcpdFolderParser();
        if (!iAmAlive()) {
            return new HashMap<String, PhpcpdResult>();
        }
        GenericOutputReader[] reader = GenericProcess.run(cmd.toString(), "", null, lookup);
        if (!iAmAlive()) {
            return new HashMap<String, PhpcpdResult>();
        }
        HashMap<String, PhpcpdResult> res = parser.parse(reader[0], folder);
        if (!iAmAlive()) {
            return new HashMap<String, PhpcpdResult>();
        }

        String staticFile = (String)PhpcpdOptions.load(PhpcpdOptions.Settings.STATIC, lookup);
        if (!staticFile.isEmpty()) {
            File staticFile2 = new File(staticFile);
            if (staticFile2.exists() && staticFile2.canRead()) {
                try {
                    Logger.getInstance().log("parsing " + staticFile + " for violoations in " + folder.getPath(), "");
                    StringBuilder content = new StringBuilder();
                    content.append(new String(Files.readAllBytes(staticFile2.toPath())));
                    HashMap<String, PhpcpdResult> res2 = parser.parse(new GenericOutputReader(content), folder);
                    for (String key : res2.keySet()) {
                        if (res.containsKey(key)) {
                            res.get(key).addAll(res2.get(key));
                        } else {
                            res.put(key, res2.get(key));
                        }
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        if (!iAmAlive()) {
            return new HashMap<String, PhpcpdResult>();
        }

        ViolationRegistry.getInstance().setPhpcpdFolder(res);
        return res;
    }

    private StringBuilder getGenericCommand(Lookup lookup) {
        StringBuilder cmd = new StringBuilder(GenericExecute.escapePath((String)PhpcpdOptions.load(PhpcpdOptions.Settings.SCRIPT, lookup)));
        this.appendArgument(cmd, "--min-lines ", "" + (Integer)PhpcpdOptions.load(PhpcpdOptions.Settings.MINLINES, lookup));
        this.appendArgument(cmd, "--min-tokens ", "" + (Integer)PhpcpdOptions.load(PhpcpdOptions.Settings.MINTOKENS, lookup));
        this.appendArgument(cmd, "--suffixes ", (String)PhpcpdOptions.load(PhpcpdOptions.Settings.SUFFIXES, lookup));

        String[] excludeOption = ((String)PhpcpdOptions.load(PhpcpdOptions.Settings.EXCLUDE, lookup)).split(",|;|\\s");
        StringBuilder exclude = new StringBuilder();
        if (excludeOption.length > 0) {
            exclude.append(excludeOption[0].trim());
            for (int i=1;i<excludeOption.length;i++) {
                exclude.append(" --exclude ").append(excludeOption[i].trim());
            }
        }
        this.appendArgument(cmd, "--exclude ", exclude.toString());
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
