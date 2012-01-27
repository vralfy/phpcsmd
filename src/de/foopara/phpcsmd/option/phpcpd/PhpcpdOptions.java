/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpcpd;

import de.foopara.phpcsmd.PHPCSMD;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author n.specht
 */
public class PhpcpdOptions {

    public static final String _PREFIX = "phpcsmd.phpcpd.";

    private static final String _ACTIVATED          = "activated";
    private static final String _ACTIVATED_DEFAULT  = "true";

    private static final String _SCRIPT             = "script";
    private static final String _SCRIPT_DEFAULT     = "/usr/bin/phpcpd";

    private static final String _MINLINES           = "minlines";
    private static final String _MINLINES_DEFAULT   = "5";

    private static final String _MINTOKENS          = "mintokens";
    private static final String _MINTOKENS_DEFAULT  = "70";

    private static final String _EXCLUDE            = "exclude";
    private static final String _EXCLUDE_DEFAULT    = "";

    private static final String _SUFFIXES           = "suffix";
    private static final String _SUFFIXES_DEFAULT   = "";

    private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public static boolean getActivated() {
        return (PhpcpdOptions._modul().get(_PREFIX + _ACTIVATED, _ACTIVATED_DEFAULT).compareTo("true") == 0);
    }

    public static void setActivated(boolean activated) {
        PhpcpdOptions._modul().put(_PREFIX + _ACTIVATED, activated ? "true" : "false");
    }

    public static String getScript() {
        return PhpcpdOptions._modul().get(_PREFIX + _SCRIPT, _SCRIPT_DEFAULT);
    }

    public static void setScript(String script) {
        PhpcpdOptions._modul().put(_PREFIX + _SCRIPT, script);
    }

    public static Integer getMinLines() {
        return Integer.parseInt(PhpcpdOptions._modul().get(_PREFIX + _MINLINES, _MINLINES_DEFAULT));
    }

    public static void setMinLines(Integer minlines) {
        PhpcpdOptions._modul().put(_PREFIX + _MINLINES, "" + minlines);
    }

    public static Integer getMinTokens() {
        return Integer.parseInt(PhpcpdOptions._modul().get(_PREFIX + _MINTOKENS, _MINTOKENS_DEFAULT));
    }

    public static void setMinTokens(Integer mintokens) {
        PhpcpdOptions._modul().put(_PREFIX + _MINTOKENS, "" + mintokens);
    }

    public static String getExcludes() {
        return PhpcpdOptions._modul().get(_PREFIX + _EXCLUDE, _EXCLUDE_DEFAULT);
    }

    public static void setExcludes(String excludes) {
        PhpcpdOptions._modul().put(_PREFIX + _EXCLUDE, excludes);
    }

    public static String getSuffixes() {
        return PhpcpdOptions._modul().get(_PREFIX + _SUFFIXES, _SUFFIXES_DEFAULT);
    }

    public static void setSuffixes(String suffixes) {
        PhpcpdOptions._modul().put(_PREFIX + _SUFFIXES, suffixes);
    }
}