/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class PhpcpdParser {

    public PhpcpdResult parse(GenericOutputReader reader) {
        List<GenericViolation> csWarnings = new ArrayList<GenericViolation>();
        List<GenericViolation> csErrors = new ArrayList<GenericViolation>();
        try {
            char[] tmp = new char[1024];
            StringBuilder buf = new StringBuilder();
            Reader r = reader.getReader();
            while (r.read(tmp) > 0) {
                buf.append(tmp);
            }
            System.out.print(buf.toString());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new PhpcpdResult(csWarnings, csErrors, null);
    }
}
