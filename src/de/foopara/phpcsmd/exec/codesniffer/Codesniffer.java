/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.codesniffer;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
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
        
        ExternalProcessBuilder epb = new ExternalProcessBuilder(CodesnifferOptions.getScript());
        epb.workingDirectory(root);
        epb.addArgument("-i");
        
        CodesnifferXMLParser parser = new CodesnifferXMLParser();
        
        CodesnifferResult res = parser.parse(null);//GenericProcess.run(epb));
        ViolationRegistry.getInstance().setCodesniffer(file, res);
        return res;
    }
}
