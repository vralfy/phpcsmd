/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.option.phpmd.PhpmdOptions;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

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
        if (!PhpmdOptions.getActivated()) {
            return new PhpmdResult(null, null);
        }
        FileUtil.toFile(file).exists();
        File script = new File(PhpmdOptions.getScript());
        if (!script.exists() || !script.canExecute() || !script.isFile()
                || !FileUtil.toFile(file).exists() || !FileUtil.toFile(file).isFile()) {
            return new PhpmdResult(null, null);
        }

        File root = FileUtil.toFile(file.getParent());

        if(root == null || this.isEnabled() == false) {
            return new GenericResult(null, null);
        }
        StringBuilder cmd = new StringBuilder(PhpmdOptions.getScript());
        cmd.append(" ").append(file.getPath());
        cmd.append(" ").append("xml");
        cmd.append(" ").append(PhpmdOptions.getRules());
        this.appendArgument(cmd, "--suffixes", PhpmdOptions.getSuffixes());
        this.appendArgument(cmd, "--exclude", PhpmdOptions.getExcludes());

        PhpmdXMLParser parser = new PhpmdXMLParser();

        PhpmdResult res = parser.parse(GenericProcess.run(cmd.toString()));
        ViolationRegistry.getInstance().setPhpmd(file, res);
        return res;
    }

    private void appendArgument(StringBuilder b, String key, String value) {
        if (value.trim().length() > 0) {
            b.append(" ").append(key).append(" ").append(value);
        }
    }
}
