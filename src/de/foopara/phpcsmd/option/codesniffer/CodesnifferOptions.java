/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.codesniffer;

import de.foopara.phpcsmd.PHPCSMD;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author n.specht
 */
public class CodesnifferOptions {

    public static final String _PREFIX = "phpcsmd.codesniffer.";

    public static final String _STANDARD           = "standard";
    public static final String _STANDARD_DEFAULT   = "Zend";

    public static final String _IGNORES            = "ignores";
    public static final String _IGNORES_DEFAULT    = "";

    public static final String _EXTENSIONS         = "extensions";
    public static final String _EXTENSIONS_DEFAULT = "";

    public static final String _WARNINGS           = "warnings";
    public static final String _WARNINGS_DEFAULT   = "true";

    private Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public String getStandard() {
        return this._modul().get(_PREFIX + _STANDARD, _STANDARD_DEFAULT);
    }

    public void setStandard(String standard) {
        this._modul().put(_PREFIX + _STANDARD, standard);
    }

    public String getIgnore() {
        return this._modul().get(_PREFIX + _IGNORES, _IGNORES_DEFAULT);
    }

    public void setIgnore(String ignore) {
        this._modul().put(_PREFIX + _IGNORES, ignore);
    }

    public String getExtensions() {
        return this._modul().get(_PREFIX + _EXTENSIONS, _EXTENSIONS_DEFAULT);
    }

    public void setExtensions(String extensions) {
        this._modul().put(_PREFIX + _EXTENSIONS, extensions);
    }

    public boolean getWarnings() {
        return (this._modul().get(_PREFIX + _WARNINGS, _WARNINGS_DEFAULT).compareTo("true") == 0);
    }

    public void setWarnings(boolean showWarnings) {
        this._modul().put(_PREFIX + _WARNINGS, showWarnings ? "true" : "false");
    }
}