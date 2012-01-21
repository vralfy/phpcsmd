/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.util.List;

/**
 *
 * @author nspecht
 */
public class GenericResult {
    
    protected List<GenericViolation> warnings = null;
    protected List<GenericViolation> errors   = null;
    
    public GenericResult(List<GenericViolation> warnings, List<GenericViolation> errors) {
        this.warnings = warnings;
        this.errors = errors;
    }
    
    public void addWarning(GenericViolation warning) {
        this.warnings.add(warning);
    }
    
    public void addError(GenericViolation error) {
        this.errors.add(error);
    }
    
    public List<GenericViolation> getWarnings() {
        return this.warnings;
    }
    
    public List<GenericViolation> getErrors() {
        return this.errors;
    }
}
