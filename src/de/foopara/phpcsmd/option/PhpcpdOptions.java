package de.foopara.phpcsmd.option;

import org.openide.util.Lookup;

public class PhpcpdOptions
{

    public static final String _PREFIX = "phpcsmd.phpcpd.";

    public enum Settings {

        ACTIVATED      ("activated",       GenericOption.SettingTypes.BOOLEAN, "false"),
        ACTIVATEDFOLDER("activatedFolder", GenericOption.SettingTypes.BOOLEAN, "false"),
        SCRIPT         ("script",          GenericOption.SettingTypes.STRING,  "/usr/bin/phpcpd"),
        MINLINES       ("minlines",        GenericOption.SettingTypes.INTEGER, "5"),
        MINTOKENS      ("mintokens",       GenericOption.SettingTypes.INTEGER, "70"),
        EXCLUDE        ("exclude",         GenericOption.SettingTypes.STRING,  ""),
        SUFFIXES       ("suffix",          GenericOption.SettingTypes.STRING,  ""),
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

    public static Object load(PhpcpdOptions.Settings key, Lookup lkp) {
        String val = GenericOption.loadMerged(_PREFIX + key.getKey(), key.getDefaultValue(), lkp);
        return GenericOption.castValue(val, key.getType());
    }

    public static Object loadOriginal(PhpcpdOptions.Settings key) {
        String val = GenericOption.loadModul(_PREFIX + key.getKey(), key.getDefaultValue());
        return GenericOption.castValue(val, key.getType());
    }

    public static void set(PhpcpdOptions.Settings key, Object value) {
        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setModul(_PREFIX + key.getKey(), val);
    }

    public static void overwrite(PhpcpdOptions.Settings key, Object value, Lookup lkp) {
        if (value == null) {
            GenericOption.flushProject(_PREFIX + key.getKey(), lkp);
            return;
        }
        String val = GenericOption.castValueToString(value, key.getType());
        GenericOption.setProject(_PREFIX + key.getKey(), val, lkp);
    }

    public static Boolean isOverwritten(PhpcpdOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + key.getKey(), lkp);
    }

}