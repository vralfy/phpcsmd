/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.DebugLog;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.option.phpcs.PhpcsOptions;
import java.io.File;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
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
        File root = FileUtil.toFile(file.getParent());
        
        if(root == null || this.isEnabled() == false) {
            return new GenericResult(null, null);
        }
        StringBuilder cmd = new StringBuilder(PhpcsOptions.getScript());
        cmd.append(" --standard=").append(PhpcsOptions.getStandard());
        if (PhpcsOptions.getExtensions().trim().length() > 0) {
            cmd.append(" --extensions=").append(PhpcsOptions.getExtensions());
        }
        if (PhpcsOptions.getIgnore().trim().length() > 0) {
            cmd.append(" --ignore=").append(PhpcsOptions.getIgnore());
        }
        cmd.append(" --report=xml");
        cmd.append(" ").append(file.getPath());
        
        /*
        ExternalProcessBuilder epb = new ExternalProcessBuilder(CodesnifferOptions.getScript());
        
        epb.workingDirectory(root);
        epb.addArgument("--standard=" + CodesnifferOptions.getStandard());
        
        if (CodesnifferOptions.getExtensions().trim().length() > 0) {
            epb.addArgument(" -extensions=" + CodesnifferOptions.getExtensions());
        }
        if (CodesnifferOptions.getIgnore().trim().length() > 0) {
            epb.addArgument("--ignore=" + CodesnifferOptions.getIgnore());
        }
        
        epb.addArgument("--report=xml");
        epb.addArgument(file.getPath());
         */
                
        PhpcsXMLParser parser = new PhpcsXMLParser();
    
        PhpcsResult res = parser.parse(GenericProcess.run(cmd.toString()));
        ViolationRegistry.getInstance().setPhpcs(file, res);
        return res;
    }
}
