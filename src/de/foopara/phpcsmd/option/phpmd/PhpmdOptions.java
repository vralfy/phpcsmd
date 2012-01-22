/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpmd;

import de.foopara.phpcsmd.PHPCSMD;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author n.specht
 */
public class PhpmdOptions {

    public static final String _PREFIX = "phpcsmd.phpmd.";

    private static final String _ACTIVATED          = "activated";
    private static final String _ACTIVATED_DEFAULT  = "true";
    
    private static final String _SCRIPT             = "script";
    private static final String _SCRIPT_DEFAULT     = "/usr/bin/phpmd";
    
    private static final String _RULES              = "rules";
    private static final String _RULES_DEFAULT      = "";

    private static final String _EXCLUDE            = "exclude";
    private static final String _EXCLUDE_DEFAULT    = "";

    private static final String _SUFFIXES           = "suffix";
    private static final String _SUFFIXES_DEFAULT   = "";

    private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public static boolean getActivated() {
        return (PhpmdOptions._modul().get(_PREFIX + _ACTIVATED, _ACTIVATED_DEFAULT).compareTo("true") == 0);
    }

    public static void setActivated(boolean activated) {
        PhpmdOptions._modul().put(_PREFIX + _ACTIVATED, activated ? "true" : "false");
    }
    
    public static String getScript() {
        return PhpmdOptions._modul().get(_PREFIX + _SCRIPT, _SCRIPT_DEFAULT);
    }

    public static void setScript(String script) {
        PhpmdOptions._modul().put(_PREFIX + _SCRIPT, script);
    }
    
    public static String getRules() {
        return PhpmdOptions._modul().get(_PREFIX + _RULES, _RULES_DEFAULT);
    }
    
    public static void setRules(String rules) {
        PhpmdOptions._modul().put(_PREFIX + _RULES, rules);
    }
    
    public static String getExcludes() {
        return PhpmdOptions._modul().get(_PREFIX + _EXCLUDE, _EXCLUDE_DEFAULT);
    }

    public static void setExcludes(String excludes) {
        PhpmdOptions._modul().put(_PREFIX + _EXCLUDE, excludes);
    }

    public static String getSuffixes() {
        return PhpmdOptions._modul().get(_PREFIX + _SUFFIXES, _SUFFIXES_DEFAULT);
    }

    public static void setSuffixes(String suffixes) {
        PhpmdOptions._modul().put(_PREFIX + _SUFFIXES, suffixes);
    }
}