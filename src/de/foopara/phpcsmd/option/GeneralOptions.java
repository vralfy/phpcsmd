package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.debug.Logger;
import org.openide.util.Lookup;

public class GeneralOptions
{

    public static final String _PREFIX = "phpcsmd.general.";

    public enum Settings {

        THREADED        ("threaded",        GenericOption.SettingTypes.BOOLEAN, "false"),
        CHECKONOPEN     ("checkonopen",     GenericOption.SettingTypes.BOOLEAN, "false"),
        UPDATEONSAVE    ("updateonsave",    GenericOption.SettingTypes.BOOLEAN, "false"),
        NOTIFY          ("notification",    GenericOption.SettingTypes.BOOLEAN, "false"),
        ERRORSTRIPE     ("errorstripe",     GenericOption.SettingTypes.BOOLEAN, "false"),
        INCLUDESTRATEGY ("includestrategy", GenericOption.SettingTypes.BOOLEAN, "false"),
        INCLUDE         ("includepattern",  GenericOption.SettingTypes.STRING,  "(?!.*\\.(svn|git))(\\.(php|css|js|php3|php4|php5))$"),
        IGNORE          ("ignorepattern",   GenericOption.SettingTypes.STRING,  "\\.(svn|git)|\\.(phtml|html|xml|js|css|xml|txt|java|svg|sql|png|jpg|gif|doc|pdf|odt)$"),
        DEBUGLOG        ("debuglog",        GenericOption.SettingTypes.BOOLEAN, "false"),
        MINSEVERITY     ("minseverity",     GenericOption.SettingTypes.INTEGER, "" + Logger.Severity.EXCEPTION.ordinal()),
        TIMEOUT         ("timeout",         GenericOption.SettingTypes.INTEGER, "5000"),
        ;

        private final String key;

        private final GenericOption.SettingTypes type;

        private final String defaultValue;

        Settings(String key, GenericOption.SettingTypes type, String defaultValue) {
            this.key = key;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return this.key;
        }

        public GenericOption.SettingTypes getType() {
            return this.type;
        }

        public String getDefaultValue() {
            return this.defaultValue;
        }
    }

    public static Object load(GeneralOptions.Settings key, Lookup lkp) {
        String val = GenericOption.loadMerged(_PREFIX + key.getKey(), key.getDefaultValue(), lkp);
        return GenericOption.castValue(val, key.getType());
    }

    public static Object loadOriginal(GeneralOptions.Settings key) {
        String val = GenericOption.loadModul(_PREFIX + key.getKey(), key.getDefaultValue());
        return GenericOption.castValue(val, key.getType());
    }

    public static void set(GeneralOptions.Settings key, Object value) {
        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setModul(_PREFIX + key.getKey(), val);
    }

    public static void overwrite(GeneralOptions.Settings key, Object value, Lookup lkp) {
        if (value == null) {
            GenericOption.flushProject(_PREFIX + key.getKey(), lkp);
            return;
        }
        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setProject(_PREFIX + key.getKey(), val, lkp);
    }

    public static Boolean isOverwritten(GeneralOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + key.getKey(), lkp);
    }

}