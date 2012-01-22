/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import org.openide.text.Annotation;

/**
 *
 * @author nspecht
 */
public class GenericViolation extends Annotation {
    protected String _message = "";
    protected int _line = 0;
    protected String _annotationType = "phpcsmd";
    
    protected String typePrefix = "de-foopara-phpcsmd-annotation-";

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

    @Override
    public String getAnnotationType() {
        return this.typePrefix + this._annotationType;
    }
    
    public GenericViolation setAnnotationType(String annotationType) {
        this._annotationType = annotationType;
        return this;
    }
}
