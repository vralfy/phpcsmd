package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.option.GenericOption.SettingTypes;
import java.util.EnumMap;
import org.openide.util.Lookup;

public class PhpmdOptions
{

    public static final String _PREFIX = "phpcsmd.phpmd.";

    public enum Settings
    {
        ACTIVATED, SCRIPT, RULES, EXCLUDE, SUFFIXES, MINPRIORITY, STRICT
    }

    private static final EnumMap<Settings, String> keys = new EnumMap<Settings, String>(Settings.class);

    static {
        keys.put(Settings.ACTIVATED, "activated");
        keys.put(Settings.SCRIPT, "script");
        keys.put(Settings.RULES, "rules");
        keys.put(Settings.EXCLUDE, "exclude");
        keys.put(Settings.SUFFIXES, "suffix");
        keys.put(Settings.MINPRIORITY, "minpriority");
        keys.put(Settings.STRICT, "strict");
    }

    private static final EnumMap<Settings, SettingTypes> types = new EnumMap<Settings, SettingTypes>(Settings.class);

    static {
        types.put(Settings.ACTIVATED, SettingTypes.BOOLEAN);
        types.put(Settings.STRICT, SettingTypes.BOOLEAN);
    }

    private static final EnumMap<Settings, String> defaults = new EnumMap<Settings, String>(Settings.class);

    static {
        defaults.put(Settings.ACTIVATED, "false");
        defaults.put(Settings.SCRIPT, "/usr/bin/phpmd");
        defaults.put(Settings.RULES, "");
        defaults.put(Settings.EXCLUDE, "");
        defaults.put(Settings.SUFFIXES, "");
        defaults.put(Settings.MINPRIORITY, "");
        defaults.put(Settings.STRICT, "false");
    }

    public static Object load(PhpmdOptions.Settings key, Lookup lkp) {
        String defaultVal = "";
        if (PhpmdOptions.defaults.containsKey(key)) {
            defaultVal = PhpmdOptions.defaults.get(key);
        }

        String val = GenericOption.loadMerged(_PREFIX + keys.get(key), defaultVal, lkp);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static Object loadOriginal(PhpmdOptions.Settings key) {
        String defaultVal = "";
        if (PhpmdOptions.defaults.containsKey(key)) {
            defaultVal = PhpmdOptions.defaults.get(key);
        }

        String val = GenericOption.loadModul(_PREFIX + keys.get(key), defaultVal);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static void set(PhpmdOptions.Settings key, Object value) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setModul(_PREFIX + keys.get(key), val);
    }

    public static void overwrite(PhpmdOptions.Settings key, Object value, Lookup lkp) {
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

    public static Boolean isOverwritten(PhpmdOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + keys.get(key), lkp);
    }

}