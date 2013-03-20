package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.debug.Logger;
import java.io.Serializable;
import java.util.HashSet;
import org.openide.util.Exceptions;

public class PdependResult implements Serializable
{
    public static final long serialVersionUID = 1L;

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
        if (file.name != null && file.name.length() > 2) {
            this._files.add(file);
        }
    }

    public HashSet<PdependTypes.PdependFile> getFiles() {
        return this._files;
    }

    public PdependTypes.PdependPackage getPackageInstanceByName(String name) {
        if (name == null) {
            try {
                throw new Exception();
            } catch (Exception ex) {
                Logger.getInstance().log(ex);
                Exceptions.printStackTrace(ex);
            }
        }
        for (PdependTypes.PdependPackage p : this._packages) {
            if (p.name.compareTo(name) == 0) {
                return p;
            }
        }

        PdependTypes.PdependPackage res = new PdependTypes.PdependPackage();
        res.name = name;
        this.addPackage(res);
        return res;
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
