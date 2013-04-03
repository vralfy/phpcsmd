package de.foopara.phpcsmd.option;

import org.openide.util.Lookup;

public class PhpcsOptions
{

    public static final String _PREFIX = "phpcsmd.phpcs.";

    public enum Settings {

        ACTIVATED   ("activated",    GenericOption.SettingTypes.BOOLEAN, "false"),
        SCRIPT      ("script",       GenericOption.SettingTypes.STRING,  "/usr/bin/phpcs"),
        STANDARD    ("standard",     GenericOption.SettingTypes.STRING,  "Zend"),
        SNIFFS      ("sniffs",       GenericOption.SettingTypes.STRING,  ""),
        IGNORES     ("ignores",      GenericOption.SettingTypes.STRING,  ""),
        EXTENSIONS  ("extensions",   GenericOption.SettingTypes.STRING,  ""),
        TABWIDTH    ("tabwidth",     GenericOption.SettingTypes.INTEGER, "-1"),
        INIOVERWRITE("inioverwrite", GenericOption.SettingTypes.STRING,  ""),
        WARNINGS    ("warnings",     GenericOption.SettingTypes.BOOLEAN, "true"),
        EXTRAS      ("extras",       GenericOption.SettingTypes.BOOLEAN, "false"),
        XSNIFF      ("sniff.",       GenericOption.SettingTypes.BOOLEAN, "false"),
        XSNIFFTASK  ("snifftask.",   GenericOption.SettingTypes.BOOLEAN, "false"),
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


    public static Object load(PhpcsOptions.Settings key, Lookup lkp) {
        String val = GenericOption.loadMerged(_PREFIX + key.getKey(), key.getDefaultValue(), lkp);
        return GenericOption.castValue(val, key.getType());
    }

    public static Object loadOriginal(PhpcsOptions.Settings key) {
        String val = GenericOption.loadModul(_PREFIX + key.getKey(), key.getDefaultValue());
        return GenericOption.castValue(val, key.getType());
    }

    public static void set(PhpcsOptions.Settings key, Object value) {
        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setModul(_PREFIX + key.getKey(), val);
    }

    public static void overwrite(PhpcsOptions.Settings key, Object value, Lookup lkp) {
        if (value == null) {
            GenericOption.flushProject(_PREFIX + key.getKey(), lkp);
            return;
        }

        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setProject(_PREFIX + key.getKey(), val, lkp);
    }

    public static Boolean isOverwritten(PhpcsOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + key.getKey(), lkp);
    }

    public static boolean getSniff(String name) {
        String val = GenericOption.loadModul(
                _PREFIX + Settings.XSNIFF.getKey() + name,
                Settings.XSNIFF.getDefaultValue());
        return (Boolean)GenericOption.castValue(val, Settings.XSNIFF.getType());
    }

    public static void setSniff(String name, boolean opt) {
        GenericOption.setModul(
                _PREFIX + Settings.XSNIFF.getKey() + name,
                GenericOption.castValueToString(opt, Settings.XSNIFF.getType()));
    }

    public static boolean getSniffTask(String name) {
        String val = GenericOption.loadModul(
                _PREFIX + Settings.XSNIFFTASK.getKey() + name,
                Settings.XSNIFFTASK.getDefaultValue());
        return (Boolean)GenericOption.castValue(val, Settings.XSNIFFTASK.getType());
    }

    public static void setSniffTask(String name, boolean opt) {
        GenericOption.setModul(
                _PREFIX + Settings.XSNIFFTASK.getKey() + name,
                GenericOption.castValueToString(opt, Settings.XSNIFFTASK.getType()));
    }

}
