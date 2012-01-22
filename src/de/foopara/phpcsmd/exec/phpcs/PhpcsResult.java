/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.PhpCsMdError;
import de.foopara.phpcsmd.PhpCsMdWarning;
import de.foopara.phpcsmd.generics.GenericResult;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class PhpcsResult extends GenericResult {
            
    public PhpcsResult(List<PhpCsMdWarning> warnings, List<PhpCsMdError> errors) {
        super(warnings,errors);
    }
    
    
}
