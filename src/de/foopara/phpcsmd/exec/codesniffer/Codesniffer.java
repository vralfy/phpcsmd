/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.codesniffer;

import de.foopara.phpcsmd.DebugLog;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericProcess;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.option.codesniffer.CodesnifferOptions;
import java.io.File;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author nspecht
 */
public class Codesniffer extends GenericExecute {
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
        StringBuilder cmd = new StringBuilder(CodesnifferOptions.getScript());
        cmd.append(" --standard=").append(CodesnifferOptions.getStandard());
        if (CodesnifferOptions.getExtensions().trim().length() > 0) {
            cmd.append(" --extensions=").append(CodesnifferOptions.getExtensions());
        }
        if (CodesnifferOptions.getIgnore().trim().length() > 0) {
            cmd.append(" --ignore=").append(CodesnifferOptions.getIgnore());
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
                
        CodesnifferXMLParser parser = new CodesnifferXMLParser();
    
        CodesnifferResult res = parser.parse(GenericProcess.run(cmd.toString()));
        ViolationRegistry.getInstance().setCodesniffer(file, res);
        return res;
    }
}
