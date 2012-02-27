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
public class PhpcsOptions {

    public static final String _PREFIX = "phpcsmd.phpcs.";

    private static final String _ACTIVATED            = "activated";
    private static final String _ACTIVATED_DEFAULT    = "false";

    private static final String _SCRIPT               = "script";
    private static final String _SCRIPT_DEFAULT       = "/usr/bin/phpcs";

    private static final String _STANDARD             = "standard";
    private static final String _STANDARD_DEFAULT     = "Zend";

    private static final String _SNIFFS               = "sniffs";
    private static final String _SNIFFS_DEFAULT       = "";

    private static final String _IGNORES              = "ignores";
    private static final String _IGNORES_DEFAULT      = "";

    private static final String _EXTENSIONS           = "extensions";
    private static final String _EXTENSIONS_DEFAULT   = "";

    private static final String _TABWIDTH             = "tabwidth";
    private static final String _TABWIDTH_DEFAULT     = "-1";

    private static final String _INIOVERWRITE         = "inioverwrite";
    private static final String _INIOVERWRITE_DEFAULT = "";

    private static final String _WARNINGS             = "warnings";
    private static final String _WARNINGS_DEFAULT     = "true";

    private static final String _EXTRAS               = "extras";
    private static final String _EXTRAS_DEFAULT       = "false";

    private static final String _XUNREACHABLE         = "xunreachable";
    private static final String _XUNREACHABLE_DEFAULT = "false";

    private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public static boolean getActivated() {
        return (PhpcsOptions._modul().get(_PREFIX + _ACTIVATED, _ACTIVATED_DEFAULT).compareTo("true") == 0);
    }

    public static void setActivated(boolean activated) {
        PhpcsOptions._modul().put(_PREFIX + _ACTIVATED, activated ? "true" : "false");
    }

    public static String getScript() {
        return PhpcsOptions._modul().get(_PREFIX + _SCRIPT, _SCRIPT_DEFAULT);
    }

    public static void setScript(String script) {
        PhpcsOptions._modul().put(_PREFIX + _SCRIPT, script);
    }

    public static String getStandard() {
        return PhpcsOptions._modul().get(_PREFIX + _STANDARD, _STANDARD_DEFAULT);
    }

    public static void setStandard(String standard) {
        PhpcsOptions._modul().put(_PREFIX + _STANDARD, standard);
    }

    public static String getSniffs() {
        return PhpcsOptions._modul().get(_PREFIX + _SNIFFS, _SNIFFS_DEFAULT);
    }

    public static void setSniffs(String sniffs) {
        PhpcsOptions._modul().put(_PREFIX + _SNIFFS, sniffs);
    }

    public static String getIgnore() {
        return PhpcsOptions._modul().get(_PREFIX + _IGNORES, _IGNORES_DEFAULT);
    }

    public static void setIgnore(String ignore) {
        PhpcsOptions._modul().put(_PREFIX + _IGNORES, ignore);
    }

    public static String getExtensions() {
        return PhpcsOptions._modul().get(_PREFIX + _EXTENSIONS, _EXTENSIONS_DEFAULT);
    }

    public static void setExtensions(String extensions) {
        PhpcsOptions._modul().put(_PREFIX + _EXTENSIONS, extensions);
    }

    public static Integer getTabwidth() {
        return Integer.parseInt(PhpcsOptions._modul().get(_PREFIX + _TABWIDTH, _TABWIDTH_DEFAULT));
    }

    public static void setTabwidth(Integer tabwidth) {
        PhpcsOptions._modul().put(_PREFIX + _TABWIDTH, "" + tabwidth);
    }

    public static String getIniOverwrite() {
        return PhpcsOptions._modul().get(_PREFIX + _INIOVERWRITE, _INIOVERWRITE_DEFAULT);
    }

    public static void setIniOverwrite(String overwrite) {
        PhpcsOptions._modul().put(_PREFIX + _INIOVERWRITE, overwrite);
    }

    public static boolean getWarnings() {
        return (PhpcsOptions._modul().get(_PREFIX + _WARNINGS, _WARNINGS_DEFAULT).compareTo("true") == 0);
    }

    public static void setWarnings(boolean showWarnings) {
        PhpcsOptions._modul().put(_PREFIX + _WARNINGS, showWarnings ? "true" : "false");
    }

    public static boolean getExtras() {
        return (PhpcsOptions._modul().get(_PREFIX + _EXTRAS, _EXTRAS_DEFAULT).compareTo("true") == 0);
    }

    public static void setExtras(boolean extras) {
        PhpcsOptions._modul().put(_PREFIX + _EXTRAS, extras ? "true" : "false");
    }

    public static boolean getXUnreachable() {
        return (PhpcsOptions._modul().get(_PREFIX + _XUNREACHABLE, _XUNREACHABLE_DEFAULT).compareTo("true") == 0);
    }

    public static void setXUnreachable(boolean unreachable) {
        PhpcsOptions._modul().put(_PREFIX + _XUNREACHABLE, unreachable ? "true" : "false");
    }
}
