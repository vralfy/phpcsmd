/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

/**
 *
 * @author nspecht
 */
public class PhpCsMdError extends GenericViolation {

    public PhpCsMdError(String msg, int line) {
        super(msg,line);
    }
    
    @Override
    public String getAnnotationType() {
        return this.typePrefix + "codesniffer-error";
    }

}
