/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import java.util.HashSet;

/**
 *
 * @author n.specht
 */
public class PdependResult {
    private PdependTypes.PdependMetrics _metrics = null;
    private HashSet<PdependTypes.PdependFile> _files = new HashSet<PdependTypes.PdependFile>();
    private HashSet<PdependTypes.PdependPackage> _packages = new HashSet<PdependTypes.PdependPackage>();
    private HashSet<PdependTypes.PdependFunction> _functions = new HashSet<PdependTypes.PdependFunction>();

    public PdependResult() {
        this._metrics = new PdependTypes.PdependMetrics();
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

    public void addPackage(PdependTypes.PdependPackage pack) {
        this._packages.add(pack);
    }

    public HashSet<PdependTypes.PdependPackage> getPackages() {
        return this._packages;
    }

    public void addFunction(PdependTypes.PdependFunction function) {
        this._functions.add(function);
    }

    public HashSet<PdependTypes.PdependFunction> getFunctions() {
        return this._functions;
    }
}
