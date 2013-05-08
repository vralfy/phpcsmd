package de.foopara.phpcsmd.exec.phpcs;

import java.util.List;

import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;

/**
 *
 * @author nspecht
 */
public class PhpcsResult extends GenericResult
{

    public PhpcsResult(List<GenericViolation> warnings, List<GenericViolation> errors, List<GenericViolation> noTask) {
        super(warnings, errors, noTask);
    }

}
