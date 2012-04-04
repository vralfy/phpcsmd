/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class PhpcpdResult extends GenericResult {

    public PhpcpdResult(List<GenericViolation> warnings, List<GenericViolation> errors, List<GenericViolation> noTask) {
        super(warnings,errors,noTask);
    }


}
