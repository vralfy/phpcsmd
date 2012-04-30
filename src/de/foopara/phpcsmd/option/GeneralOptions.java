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

    private static final String _NOTIFICATION            = "notification";
    private static final String _NOTIFICATION_DEFAULT    = "false";

    private static final String _IGNORE_PATTERN          = "ignorepattern";
    private static final String _IGNORE_PATTERN_DEFAULT  = "\\.svn|\\.git";

    private static final String _TIMEOUT                 = "timeout";
    private static final String _TIMEOUT_DEFAULT         = "5000";


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

    public static void setUpdateOnSave(boolean updateOnSave) {
        GeneralOptions._modul().put(_PREFIX + _UPDATE_ON_SAVE, updateOnSave ? "true" : "false");
    }

    public static boolean getNotification() {
        return (GeneralOptions._modul().get(_PREFIX + _NOTIFICATION, _NOTIFICATION_DEFAULT).compareTo("true") == 0);
    }

    public static void setNotification(boolean notification) {
        GeneralOptions._modul().put(_PREFIX + _NOTIFICATION, notification ? "true" : "false");
    }

    public static String getIgnorePattern() {
        return GeneralOptions._modul().get(_PREFIX + _IGNORE_PATTERN, _IGNORE_PATTERN_DEFAULT);
    }

    public static void setIgnorePattern(String ignorePattern) {
        GeneralOptions._modul().put(_PREFIX + _IGNORE_PATTERN, ignorePattern);
    }

    public static Integer getTimeout() {
        return Integer.parseInt(GeneralOptions._modul().get(_PREFIX + _TIMEOUT, _TIMEOUT_DEFAULT));
    }

    public static void setTimeout(Integer ignorePattern) {
        GeneralOptions._modul().put(_PREFIX + _TIMEOUT, "" + ignorePattern);
    }
}