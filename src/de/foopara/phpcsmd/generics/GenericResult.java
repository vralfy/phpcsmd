package de.foopara.phpcsmd.generics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class GenericResult
{

    protected List<GenericViolation> warnings = null;

    protected List<GenericViolation> errors = null;

    protected List<GenericViolation> noTask = null;

    public GenericResult(List<GenericViolation> warnings, List<GenericViolation> errors, List<GenericViolation> noTask) {
        if (warnings == null) {
            warnings = new ArrayList<GenericViolation>();
        }
        if (errors == null) {
            errors = new ArrayList<GenericViolation>();
        }
        if (noTask == null) {
            noTask = new ArrayList<GenericViolation>();
        }
        this.warnings = warnings;
        this.errors = errors;
        this.noTask = noTask;
    }

    public void addWarning(GenericViolation warning) {
        this.warnings.add(warning);
    }

    public void addError(GenericViolation error) {
        this.errors.add(error);
    }

    public void addNoTask(GenericViolation noTask) {
        this.errors.add(noTask);
    }

    public List<GenericViolation> getWarnings() {
        return this.warnings;
    }

    public List<GenericViolation> getErrors() {
        return this.errors;
    }

    public List<GenericViolation> getNoTask() {
        return this.noTask;
    }

    public int getSum() {
        return this.errors.size() + this.warnings.size();
    }

}