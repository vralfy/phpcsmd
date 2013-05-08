package de.foopara.phpcsmd.exec.phpcpd;

import java.util.List;

import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;

/**
 *
 * @author nspecht
 */
public class PhpcpdResult extends GenericResult
{

    public PhpcpdResult(List<GenericViolation> warnings, List<GenericViolation> errors, List<GenericViolation> noTask) {
        super(warnings, errors, noTask);
    }

}