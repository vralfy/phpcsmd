/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.PHPCSMD;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author n.specht
 */
public class PdependOptions {

    public static final String _PREFIX = "phpcsmd.pdepend.";

    private static final String _SCRIPT             = "script";
    private static final String _SCRIPT_DEFAULT     = "/usr/bin/pdepend";

    private static final String _EXCLUDE            = "exclude";
    private static final String _EXCLUDE_DEFAULT    = "";

    private static final String _SUFFIXES           = "suffix";
    private static final String _SUFFIXES_DEFAULT   = "";

    private static final String _IGNORES            = "ignore";
    private static final String _IGNORES_DEFAULT    = "";

    private static final String _INIOVERWRITE         = "inioverwrite";
    private static final String _INIOVERWRITE_DEFAULT = "";

    private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public static String getScript() {
        return PdependOptions._modul().get(_PREFIX + _SCRIPT, _SCRIPT_DEFAULT);
    }

    public static void setScript(String script) {
        PdependOptions._modul().put(_PREFIX + _SCRIPT, script);
    }

    public static String getExcludes() {
        return PdependOptions._modul().get(_PREFIX + _EXCLUDE, _EXCLUDE_DEFAULT);
    }

    public static void setExcludes(String excludes) {
        PdependOptions._modul().put(_PREFIX + _EXCLUDE, excludes);
    }

    public static String getSuffixes() {
        return PdependOptions._modul().get(_PREFIX + _SUFFIXES, _SUFFIXES_DEFAULT);
    }

    public static void setSuffixes(String suffixes) {
        PdependOptions._modul().put(_PREFIX + _SUFFIXES, suffixes);
    }

    public static String getIgnores() {
        return PdependOptions._modul().get(_PREFIX + _IGNORES, _IGNORES_DEFAULT);
    }

    public static void setIgnores(String ignores) {
        PdependOptions._modul().put(_PREFIX + _IGNORES, ignores);
    }

    public static String getIniOverwrite() {
        return PdependOptions._modul().get(_PREFIX + _INIOVERWRITE, _INIOVERWRITE_DEFAULT);
    }

    public static void setIniOverwrite(String overwrite) {
        PdependOptions._modul().put(_PREFIX + _INIOVERWRITE, overwrite);
    }
}