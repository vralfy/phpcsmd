package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.debug.Logger;
import java.util.ArrayList;
import org.openide.text.Annotation;

/**
 *
 * @author nspecht
 */
public class GenericViolation extends Annotation
{

    protected String _message = "";

    protected int _beginLine = 0;

    protected int _endLine = 0;

    protected String _annotationType = "generic";

    protected String _group = "generic";

    protected boolean multiline = true;

    protected String typePrefix = "de-foopara-phpcsmd-annotation-";

    protected String groupPrefix = "de-foopara-phpcsmd-group-";

    ArrayList<GenericViolation> children = new ArrayList<GenericViolation>();

    public GenericViolation(String msg, int line) {
        this._message = msg;
        this._beginLine = line;
        this._endLine = line;
    }

    public GenericViolation(String msg, int begin, int end) {
        this._message = msg;
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
        if (this.multiline) {
            return this._endLine;
        }
        return this.getBeginLine();
    }

    @Override
    public String getAnnotationType() {
        return this.typePrefix + this._annotationType;
    }

    public String getTaskGroup() {
        return this.groupPrefix + this._group;
    }

    public GenericViolation setAnnotationType(String annotationType) {
        this._annotationType = annotationType;
        return this;
    }

    public GenericViolation setGroup(String group) {
        this._group = group;
        return this;
    }

    public GenericViolation getViolationForLine(int line) {
        if (line == this._beginLine) {
            return this;
        }
        GenericViolation child = new GenericViolation(this._message, line)
                .setAnnotationType(this._annotationType)
                .setGroup(this._group);
        this.children.add(child);
        return child;
    }

    public void detachMe() {
        try {
            super.detach();
        } catch (IllegalStateException ex) {
            Logger.getInstance().log(ex);
        } catch (Exception ex) {
            Logger.getInstance().log(ex);
        }
    }

    public GenericViolation detachChildren() {
        for (int i = 0; i < this.children.size(); i++) {
            try {
                this.children.get(i).detachMe();
            } catch (IllegalStateException ex) {
                Logger.getInstance().log(ex);
            } catch (Exception ex) {
                Logger.getInstance().log(ex);
            }

            try {
                this.children.get(i).detachChildren();
            } catch (IllegalStateException ex) {
                Logger.getInstance().log(ex);
            } catch (Exception ex) {
                Logger.getInstance().log(ex);
            }
        }
        this.children.clear();
        return this;
    }

    public GenericViolation setMultiline(boolean multiline) {
        this.multiline = multiline;
        return this;
    }

}
