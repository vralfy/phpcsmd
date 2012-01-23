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
public class GeneralOptions {

    public static final String _PREFIX = "phpcsmd.general.";

    private static final String _THREADED                = "threaded";
    private static final String _THREADED_DEFAULT        = "false";

    private static final String _UPDATE_ON_SAVE          = "updateonsave";
    private static final String _UPDATE_ON_SAVE_DEFAULT  = "false";

     private static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    public static boolean getThreaded() {
        return (GeneralOptions._modul().get(_PREFIX + _THREADED, _THREADED_DEFAULT).compareTo("true") == 0);
    }

    public static void setThreaded(boolean threaded) {
        GeneralOptions._modul().put(_PREFIX + _THREADED, threaded ? "true" : "false");
    }

    public static boolean getUpdateOnSave() {
        return (GeneralOptions._modul().get(_PREFIX + _UPDATE_ON_SAVE, _UPDATE_ON_SAVE_DEFAULT).compareTo("true") == 0);
    }

    public static void setupdateOnSave(boolean updateOnSave) {
        GeneralOptions._modul().put(_PREFIX + _UPDATE_ON_SAVE, updateOnSave ? "true" : "false");
    }
}