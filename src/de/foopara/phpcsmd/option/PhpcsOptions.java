package de.foopara.phpcsmd.option;

import java.util.EnumMap;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class PhpcsOptions {

    public static final String _PREFIX = "phpcsmd.phpcs.";

    public enum Settings {
        ACTIVATED, SCRIPT, STANDARD, SNIFFS, IGNORES, EXTENSIONS, TABWIDTH, INIOVERWRITE,
        WARNINGS, EXTRAS, XSNIFF, XSNIFFTASK
    }

    private static final EnumMap<PhpcsOptions.Settings, String> keys = new EnumMap<PhpcsOptions.Settings, String>(PhpcsOptions.Settings.class);
    static {
        keys.put(PhpcsOptions.Settings.ACTIVATED, "activated");
        keys.put(PhpcsOptions.Settings.SCRIPT, "script");
        keys.put(PhpcsOptions.Settings.STANDARD, "standard");
        keys.put(PhpcsOptions.Settings.SNIFFS, "sniffs");
        keys.put(PhpcsOptions.Settings.IGNORES, "ignores");
        keys.put(PhpcsOptions.Settings.EXTENSIONS, "extensions");
        keys.put(PhpcsOptions.Settings.TABWIDTH, "tabwidth");
        keys.put(PhpcsOptions.Settings.INIOVERWRITE, "inioverwrite");
        keys.put(PhpcsOptions.Settings.WARNINGS, "warnings");
        keys.put(PhpcsOptions.Settings.EXTRAS, "extras");
        keys.put(PhpcsOptions.Settings.XSNIFF, "sniff.");
        keys.put(PhpcsOptions.Settings.XSNIFFTASK, "snifftask.");
    }

    private static final EnumMap<PhpcsOptions.Settings, GenericOption.SettingTypes> types = new EnumMap<PhpcsOptions.Settings, GenericOption.SettingTypes>(PhpcsOptions.Settings.class);
    static {
        types.put(PhpcsOptions.Settings.ACTIVATED, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcsOptions.Settings.TABWIDTH, GenericOption.SettingTypes.INTEGER);
        types.put(PhpcsOptions.Settings.WARNINGS, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcsOptions.Settings.EXTRAS, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcsOptions.Settings.XSNIFF, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcsOptions.Settings.XSNIFFTASK, GenericOption.SettingTypes.BOOLEAN);
    }

    private static final EnumMap<PhpcsOptions.Settings, String> defaults = new EnumMap<PhpcsOptions.Settings, String>(PhpcsOptions.Settings.class);
    static {
        defaults.put(PhpcsOptions.Settings.ACTIVATED, "false");
        defaults.put(PhpcsOptions.Settings.SCRIPT, "/usr/bin/phpcs");
        defaults.put(PhpcsOptions.Settings.STANDARD, "Zend");
        defaults.put(PhpcsOptions.Settings.SNIFFS, "");
        defaults.put(PhpcsOptions.Settings.IGNORES, "");
        defaults.put(PhpcsOptions.Settings.EXTENSIONS, "");
        defaults.put(PhpcsOptions.Settings.TABWIDTH, "-1");
        defaults.put(PhpcsOptions.Settings.INIOVERWRITE, "");
        defaults.put(PhpcsOptions.Settings.WARNINGS, "true");
        defaults.put(PhpcsOptions.Settings.EXTRAS, "false");
        defaults.put(PhpcsOptions.Settings.XSNIFF, "false");
        defaults.put(PhpcsOptions.Settings.XSNIFFTASK, "true");
    }

    public static Object load(PhpcsOptions.Settings key, Lookup lkp) {
        String defaultVal = "";
        if (PhpcsOptions.defaults.containsKey(key)) {
            defaultVal = PhpcsOptions.defaults.get(key);
        }

        String val = GenericOption.loadMerged(_PREFIX + keys.get(key), defaultVal, lkp);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static Object loadOriginal(PhpcsOptions.Settings key) {
        String defaultVal = "";
        if (PhpcsOptions.defaults.containsKey(key)) {
            defaultVal = PhpcsOptions.defaults.get(key);
        }

        String val = GenericOption.loadModul(_PREFIX +keys.get(key), defaultVal);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static void set(PhpcsOptions.Settings key, Object value) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setModul(_PREFIX + keys.get(key), val);
    }

    public static void overwrite(PhpcsOptions.Settings key, Object value, Lookup lkp) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setProject(_PREFIX + keys.get(key), val, lkp);
    }

    public static boolean getSniff(String name) {
        String val = GenericOption.loadModul(
                _PREFIX + keys.get(Settings.XSNIFF) + name,
                defaults.get(Settings.XSNIFF));
        return (Boolean)GenericOption.castValue(val, types.get(Settings.XSNIFF));
    }

    public static void setSniff(String name, boolean opt) {
        GenericOption.setModul(
                _PREFIX + keys.get(Settings.XSNIFF) + name,
                GenericOption.castValueToString(opt, types.get(Settings.XSNIFF)));
    }

    public static boolean getSniffTask(String name) {
        String val = GenericOption.loadModul(
                _PREFIX + keys.get(Settings.XSNIFFTASK) + name,
                defaults.get(Settings.XSNIFFTASK));
        return (Boolean)GenericOption.castValue(val, types.get(Settings.XSNIFFTASK));
    }

    public static void setSniffTask(String name, boolean opt) {
        GenericOption.setModul(
                _PREFIX + keys.get(Settings.XSNIFFTASK) + name,
                GenericOption.castValueToString(opt, types.get(Settings.XSNIFFTASK)));
    }
}
