/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.util.ArrayList;
import org.openide.text.Annotation;

/**
 *
 * @author nspecht
 */
public class GenericViolation extends Annotation {
    protected String _message = "";
    protected int _beginLine = 0;
    protected int _endLine = 0;
    protected String _annotationType = "generic";

    protected String typePrefix = "de-foopara-phpcsmd-annotation-";

    ArrayList<GenericViolation> children = new ArrayList<GenericViolation>();

    public GenericViolation(String msg, int line) {
        this._message   = msg;
        this._beginLine = line;
        this._endLine   = line;
    }

    public GenericViolation(String msg, int begin, int end) {
        this._message   = msg;
        this._beginLine = begin;
        this._endLine = end;
    }

    @Override
    public String getShortDescription() {
        return this._message;
    }

    public int getBeginLine() {
        return this._beginLine;
    }

    public int getEndLine() {
        return this._endLine;
    }

    @Override
    public String getAnnotationType() {
        return this.typePrefix + this._annotationType;
    }

    public String getTaskGroup() {
        return "Error";
    }

    public GenericViolation setAnnotationType(String annotationType) {
        this._annotationType = annotationType;
        return this;
    }

    public GenericViolation getViolationForLine(int line) {
        if (line == this._beginLine) return this;
        GenericViolation child = new GenericViolation(this._message, line).setAnnotationType(this._annotationType);
        this.children.add(child);
        return child;
    }

    public void detachChildren() {
        for (int i=0; i<this.children.size();i++) {
            this.children.get(i).detach();
            this.children.get(i).detachChildren();
        }
        this.children.clear();
    }
}
