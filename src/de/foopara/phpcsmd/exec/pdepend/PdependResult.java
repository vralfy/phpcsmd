/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import java.util.HashSet;
import org.openide.util.Exceptions;

/**
 *
 * @author n.specht
 */
public class PdependResult {
    private PdependTypes.PdependMetrics _metrics = null;
    private HashSet<PdependTypes.PdependFile> _files = new HashSet<PdependTypes.PdependFile>();

    public PdependResult() {
        try {
            this._metrics = PdependTypes.PdependMetrics.class.newInstance();
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void setMetrics(PdependTypes.PdependMetrics metric) {
        this._metrics = metric;
    }

    public PdependTypes.PdependMetrics getMetrics() {
        return this._metrics;
    }

    public void addFile(PdependTypes.PdependFile file) {
        this._files.add(file);
    }

    public HashSet<PdependTypes.PdependFile> getFiles() {
        return this._files;
    }
}
