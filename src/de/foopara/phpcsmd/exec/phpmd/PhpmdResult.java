package de.foopara.phpcsmd.exec.phpmd;

import java.util.List;

import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;

/**
 *
 * @author nspecht
 */
public class PhpmdResult extends GenericResult
{

    public PhpmdResult(List<GenericViolation> warnings, List<GenericViolation> errors, List<GenericViolation> noTask) {
        super(warnings, errors, noTask);
    }

}
