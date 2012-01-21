/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.codesniffer;

import de.foopara.phpcsmd.PhpCsMdError;
import de.foopara.phpcsmd.PhpCsMdWarning;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class CodesnifferXMLParser {
    
    public CodesnifferResult parse(Reader reader) {
        List<PhpCsMdWarning> csWarnings = new ArrayList<PhpCsMdWarning>();
        List<PhpCsMdError> csErrors = new ArrayList<PhpCsMdError>();
        
        csWarnings.add(new PhpCsMdWarning("TestWarnung", 2));
        csErrors.add(new PhpCsMdError("TestFehler", 3));
        
        return new CodesnifferResult(csWarnings, csErrors);
    }
}
