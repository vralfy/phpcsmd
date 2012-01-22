/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpcs;

import de.foopara.phpcsmd.PHPCSMD;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author n.specht
 */
public class PhpcsOptions {

    public static final String _PREFIX = "phpcsmd.phpcs.";

    private static final String _SCRIPT             = "script";
    private static final String _SCRIPT_DEFAULT     = "/usr/bin/phpcs";
    
    private static final String _STANDARD           = "standard";
    private static final String _STANDARD_DEFAULT   = "Zend";

    private static final String _IGNORES            = "ignores";
    private static final String _IGNORES_DEFAULT    = "";

    private static final String _EXTENSIONS         = "extensions";
    private static final String _EXTENSIONS_DEFAULT = "";

    private static final String _WARNINGS           = "warnings";
    private static final String _WARNINGS_DEFAULT   = "true";

    private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
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

    public static boolean getWarnings() {
        return (PhpcsOptions._modul().get(_PREFIX + _WARNINGS, _WARNINGS_DEFAULT).compareTo("true") == 0);
    }

    public static void setWarnings(boolean showWarnings) {
        PhpcsOptions._modul().put(_PREFIX + _WARNINGS, showWarnings ? "true" : "false");
    }
}