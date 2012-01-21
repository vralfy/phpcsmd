/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.codesniffer;

import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class CodesnifferResult extends GenericResult {
    
    public static CodesnifferResult newInstance() {
        return new CodesnifferResult(new ArrayList<GenericViolation>(), new ArrayList<GenericViolation>());
    }
        
    public CodesnifferResult(List<GenericViolation> warnings, List<GenericViolation> errors) {
        super(warnings,errors);
    }
    
    
}
