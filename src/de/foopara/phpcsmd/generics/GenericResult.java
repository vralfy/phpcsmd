/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.PhpCsMdError;
import de.foopara.phpcsmd.PhpCsMdWarning;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class GenericResult {
    
    protected List<PhpCsMdWarning> warnings = null;
    protected List<PhpCsMdError> errors   = null;
    
    public GenericResult(List<PhpCsMdWarning> warnings, List<PhpCsMdError> errors) {
        if (warnings == null) {
            warnings = new ArrayList<PhpCsMdWarning>();
        }
        if (errors == null) {
            errors = new ArrayList<PhpCsMdError>();
        }
        this.warnings = warnings;
        this.errors = errors;
    }
    
    public void addWarning(PhpCsMdWarning warning) {
        this.warnings.add(warning);
    }
    
    public void addError(PhpCsMdError error) {
        this.errors.add(error);
    }
    
    public List<PhpCsMdWarning> getWarnings() {
        return this.warnings;
    }
    
    public List<PhpCsMdError> getErrors() {
        return this.errors;
    }
}
