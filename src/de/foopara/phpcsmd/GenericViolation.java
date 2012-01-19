/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import org.openide.text.Annotation;

/**
 *
 * @author nspecht
 */
abstract public class GenericViolation extends Annotation {
    private String _message = "";
    private int _line = 0;
    
    public GenericViolation(String msg, int line) {
        this._message = msg;
        this._line    = line;
    }
    
    @Override
    public String getShortDescription() {
        return this._message;
    }
    
    public int getLine() {
        return this._line;
    }
}
