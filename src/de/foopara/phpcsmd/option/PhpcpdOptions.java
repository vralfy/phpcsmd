package de.foopara.phpcsmd.option;

import java.util.EnumMap;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class PhpcpdOptions {

    public static final String _PREFIX = "phpcsmd.phpcpd.";

    public enum Settings {
        ACTIVATED, ACTIVATEDFOLDER, SCRIPT, MINLINES, MINTOKENS, EXCLUDE, SUFFIXES
    }

    private static final EnumMap<PhpcpdOptions.Settings, String> keys = new EnumMap<PhpcpdOptions.Settings, String>(PhpcpdOptions.Settings.class);
    static {
        keys.put(PhpcpdOptions.Settings.ACTIVATED, "activated");
        keys.put(PhpcpdOptions.Settings.ACTIVATEDFOLDER, "activatedFolder");
        keys.put(PhpcpdOptions.Settings.SCRIPT, "script");
        keys.put(PhpcpdOptions.Settings.MINLINES, "minlines");
        keys.put(PhpcpdOptions.Settings.MINTOKENS, "mintokens");
        keys.put(PhpcpdOptions.Settings.EXCLUDE, "exclude");
        keys.put(PhpcpdOptions.Settings.SUFFIXES, "suffix");

    }

    private static final EnumMap<PhpcpdOptions.Settings, GenericOption.SettingTypes> types = new EnumMap<PhpcpdOptions.Settings, GenericOption.SettingTypes>(PhpcpdOptions.Settings.class);
    static {
        types.put(PhpcpdOptions.Settings.ACTIVATED, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcpdOptions.Settings.ACTIVATEDFOLDER, GenericOption.SettingTypes.BOOLEAN);
        types.put(PhpcpdOptions.Settings.MINLINES, GenericOption.SettingTypes.INTEGER);
        types.put(PhpcpdOptions.Settings.MINTOKENS, GenericOption.SettingTypes.INTEGER);
    }

    private static final EnumMap<PhpcpdOptions.Settings, String> defaults = new EnumMap<PhpcpdOptions.Settings, String>(PhpcpdOptions.Settings.class);
    static {
        defaults.put(PhpcpdOptions.Settings.ACTIVATED, "false");
        defaults.put(PhpcpdOptions.Settings.ACTIVATEDFOLDER, "false");
        defaults.put(PhpcpdOptions.Settings.SCRIPT, "/usr/bin/phpcpd");
        defaults.put(PhpcpdOptions.Settings.MINLINES, "5");
        defaults.put(PhpcpdOptions.Settings.MINTOKENS, "70");
        defaults.put(PhpcpdOptions.Settings.EXCLUDE, "");
        defaults.put(PhpcpdOptions.Settings.SUFFIXES, "");
    }

    public static Object load(PhpcpdOptions.Settings key, Lookup lkp) {
        String defaultVal = "";
        if (PhpcpdOptions.defaults.containsKey(key)) {
            defaultVal = PhpcpdOptions.defaults.get(key);
        }

        String val = GenericOption.loadMerged(_PREFIX + keys.get(key), defaultVal, lkp);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static Object loadOriginal(PhpcpdOptions.Settings key) {
        String defaultVal = "";
        if (PhpcpdOptions.defaults.containsKey(key)) {
            defaultVal = PhpcpdOptions.defaults.get(key);
        }

        String val = GenericOption.loadModul(_PREFIX +keys.get(key), defaultVal);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static void set(PhpcpdOptions.Settings key, Object value) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setModul(_PREFIX + keys.get(key), val);
    }

    public static void overwrite(PhpcpdOptions.Settings key, Object value, Lookup lkp) {
        if (value == null) {
            GenericOption.flushProject(_PREFIX + keys.get(key), lkp);
            return;
        }
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setProject(_PREFIX + keys.get(key), val, lkp);
    }

    public static Boolean isOverwritten(PhpcpdOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + keys.get(key), lkp);
    }
}