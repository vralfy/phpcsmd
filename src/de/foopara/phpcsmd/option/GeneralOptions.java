package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.debug.Logger;
import java.util.EnumMap;
import org.openide.util.Lookup;

public class GeneralOptions
{

    public static final String _PREFIX = "phpcsmd.general.";

    public enum Settings
    {

        THREADED, UPDATEONSAVE, NOTIFY, ERRORSTRIPE, IGNORE, DEBUGLOG, MINSEVERITY, TIMEOUT

    }

    private static final EnumMap<GeneralOptions.Settings, String> keys = new EnumMap<GeneralOptions.Settings, String>(GeneralOptions.Settings.class);

    static {
        keys.put(GeneralOptions.Settings.THREADED, "threaded");
        keys.put(GeneralOptions.Settings.UPDATEONSAVE, "updateonsave");
        keys.put(GeneralOptions.Settings.NOTIFY, "notification");
        keys.put(GeneralOptions.Settings.ERRORSTRIPE, "errorstripe");
        keys.put(GeneralOptions.Settings.IGNORE, "ignorepattern");
        keys.put(GeneralOptions.Settings.DEBUGLOG, "debuglog");
        keys.put(GeneralOptions.Settings.MINSEVERITY, "minseverity");
        keys.put(GeneralOptions.Settings.TIMEOUT, "timeout");

    }

    private static final EnumMap<GeneralOptions.Settings, GenericOption.SettingTypes> types = new EnumMap<GeneralOptions.Settings, GenericOption.SettingTypes>(GeneralOptions.Settings.class);

    static {
        types.put(GeneralOptions.Settings.THREADED, GenericOption.SettingTypes.BOOLEAN);
        types.put(GeneralOptions.Settings.UPDATEONSAVE, GenericOption.SettingTypes.BOOLEAN);
        types.put(GeneralOptions.Settings.NOTIFY, GenericOption.SettingTypes.BOOLEAN);
        types.put(GeneralOptions.Settings.ERRORSTRIPE, GenericOption.SettingTypes.BOOLEAN);
        types.put(GeneralOptions.Settings.DEBUGLOG, GenericOption.SettingTypes.BOOLEAN);
        types.put(GeneralOptions.Settings.MINSEVERITY, GenericOption.SettingTypes.INTEGER);
        types.put(GeneralOptions.Settings.TIMEOUT, GenericOption.SettingTypes.INTEGER);
    }

    private static final EnumMap<GeneralOptions.Settings, String> defaults = new EnumMap<GeneralOptions.Settings, String>(GeneralOptions.Settings.class);

    static {
        defaults.put(GeneralOptions.Settings.THREADED, "false");
        defaults.put(GeneralOptions.Settings.UPDATEONSAVE, "false");
        defaults.put(GeneralOptions.Settings.NOTIFY, "false");
        defaults.put(GeneralOptions.Settings.ERRORSTRIPE, "false");
        defaults.put(GeneralOptions.Settings.IGNORE, "\\.(svn|git)|\\.(phtml|html|js|css|xml|txt|java|svg|png|jpg|gif)$");
        defaults.put(GeneralOptions.Settings.DEBUGLOG, "false");
        defaults.put(GeneralOptions.Settings.MINSEVERITY, "" + Logger.Severity.EXCEPTION.ordinal());
        defaults.put(GeneralOptions.Settings.TIMEOUT, "5000");
    }

    public static Object load(GeneralOptions.Settings key, Lookup lkp) {
        String defaultVal = "";
        if (GeneralOptions.defaults.containsKey(key)) {
            defaultVal = GeneralOptions.defaults.get(key);
        }

        String val = GenericOption.loadMerged(_PREFIX + keys.get(key), defaultVal, lkp);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static Object loadOriginal(GeneralOptions.Settings key) {
        String defaultVal = "";
        if (GeneralOptions.defaults.containsKey(key)) {
            defaultVal = GeneralOptions.defaults.get(key);
        }

        String val = GenericOption.loadModul(_PREFIX + keys.get(key), defaultVal);

        if (!types.containsKey(key)) {
            return val;
        }

        return GenericOption.castValue(val, types.get(key));
    }

    public static void set(GeneralOptions.Settings key, Object value) {
        String val = value.toString();
        if (types.containsKey(key)) {
            val = GenericOption.castValueToString(value, types.get(key));
        }
        GenericOption.setModul(_PREFIX + keys.get(key), val);
    }

    public static void overwrite(GeneralOptions.Settings key, Object value, Lookup lkp) {
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

    public static Boolean isOverwritten(GeneralOptions.Settings key, Lookup lkp) {
        return GenericOption.isInProject(_PREFIX + keys.get(key), lkp);
    }

}