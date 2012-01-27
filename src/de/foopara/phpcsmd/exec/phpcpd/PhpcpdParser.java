/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class PhpcpdParser {

    public PhpcpdResult parse(GenericOutputReader reader) {
        List<GenericViolation> csWarnings = new ArrayList<GenericViolation>();
        List<GenericViolation> csErrors = new ArrayList<GenericViolation>();

        /**/

        return new PhpcpdResult(csWarnings, csErrors);
    }
}
