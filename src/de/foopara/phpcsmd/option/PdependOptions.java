package de.foopara.phpcsmd.option;

import java.util.EnumMap;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class PdependOptions {

    public static final String _PREFIX = "phpcsmd.pdepend.";

   public enum Settings {
        SCRIPT, EXCLUDE, SUFFIXES, IGNORES, INIOVERWRITE, USETABS, JDEPEND
    }

    private static final EnumMap<PdependOptions.Settings, String> keys = new EnumMap<PdependOptions.Settings, String>(PdependOptions.Settings.class);
    static {
        keys.put(PdependOptions.Settings.SCRIPT, "script");
        keys.put(PdependOptions.Settings.EXCLUDE, "exclude");
        keys.put(PdependOptions.Settings.SUFFIXES, "suffix");
        keys.put(PdependOptions.Settings.IGNORES, "ignore");
        keys.put(PdependOptions.Settings.INIOVERWRITE, "inioverwrite");
        keys.put(PdependOptions.Settings.USETABS, "usetabs");
        keys.put(PdependOptions.Settings.JDEPEND, "jdepend");

    }

    private static final EnumMap<PdependOptions.Settings, GenericOption.SettingTypes> types = new EnumMap<PdependOptions.Settings, GenericOption.SettingTypes>(PdependOptions.Settings.class);
    static {
        types.put(PdependOptions.Settings.USETABS, GenericOption.SettingTypes.BOOLEAN);
        types.put(PdependOptions.Settings.JDEPEND, GenericOption.SettingTypes.BOOLEAN);
    }

    private static final EnumMap<PdependOptions.Settings, String> defaults = new EnumMap<PdependOptions.Settings, String>(PdependOptions.Settings.class);
    static {
        defaults.put(PdependOptions.Settings.SCRIPT, "/usr/bin/pdepend");
        defaults.put(PdependOptions.Settings.EXCLUDE, "");
        defaults.put(PdependOptions.Settings.SUFFIXES, "");
        defaults.put(PdependOptions.Settings.IGNORES, "");
        defaults.put(PdependOptions.Settings.INIOVERWRITE, "");
        defaults.put(PdependOptions.Settings.USETABS, "false");
        defaults.put(PdependOptions.Settings.JDEPEND, "false");
    }

    public static Object load(PdependOptions.Settings key, Lookup lkp) {
        String defaultVal = "";
        if (PdependOptions.defaults.containsKey(key)) {
            defaultVal = PdependOptions.defaults.get(key);
        }

        String val = GenericOption.loadMerged(_PREFIX + keys.get(key), defaultVal, lkp);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static Object loadOriginal(PdependOptions.Settings key) {
        String defaultVal = "";
        if (PdependOptions.defaults.containsKey(key)) {
            defaultVal = PdependOptions.defaults.get(key);
        }

        String val = GenericOption.loadModul(_PREFIX +keys.get(key), defaultVal);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static void set(PdependOptions.Settings key, Object value) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setModul(_PREFIX + keys.get(key), val);
    }

    public static void overwrite(PdependOptions.Settings key, Object value, Lookup lkp) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setProject(_PREFIX + keys.get(key), val, lkp);
    }
}