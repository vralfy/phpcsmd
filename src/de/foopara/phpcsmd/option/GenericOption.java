/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.PHPCSMD;
import de.foopara.phpcsmd.debug.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
/**
 *
 * @author nspecht
 */
abstract public class GenericOption
{
    public static enum SettingTypes {
        BOOLEAN, INTEGER, STRING
    }

    protected static Preferences _modul() {
        return NbPreferences.forModule(PHPCSMD.class);
    }

    protected static ResourceBundle _project() {
        return NbBundle.getBundle(PHPCSMD.class);
    }

    protected static String loadMerged(String id, String def, Lookup lkp) {
        return GenericOption.loadProject(id, GenericOption._modul().get(id, def), lkp);
    }

    protected static String loadModul(String id, String def) {
        return GenericOption._modul().get(id, def);
    }

    protected static void setModul(String id, String value) {
        GenericOption._modul().put(id, value);
    }

    protected static String loadProject(String id, String def, Lookup lkp) {
        File config = GenericOption.getProjectProperties(lkp);
        if (config == null) {
            return def;
        }

        Properties p = new Properties();
        try {
            p.load(new FileInputStream(config));
        } catch (FileNotFoundException ex) {
            Logger.getInstance().log(ex);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        }
        return p.getProperty(id, def);
    }

    protected static void setProject(String id, String value, Lookup lkp) {
        try {
            File config = GenericOption.getProjectProperties(lkp);
            if (config != null) {
                Properties p = new Properties();
                p.load(new FileInputStream(config));
                p.setProperty(id, value);
                p.store(new FileOutputStream(config), "Phpcsmd options");
            }
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        }
    }

    protected static File getProjectProperties(Lookup lkp) {
        if (lkp == null) {
            Logger.getInstance().log(new Exception("Lookup is null."));
            return null;
        }
        Project project = GenericOption.getProjectFromLookup(lkp);
        if (project == null) {
            Logger.getInstance().log(new Exception("Project was not found in lookup."));
            return null;
        }
        FileObject fo = project.getProjectDirectory();

        File config = new File(FileUtil.toFile(fo), "nbproject/phpcsmd.properties");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException ex) {
                Logger.getInstance().log(ex);
            }
        }
        return config;
    }

    protected static Project getProjectFromLookup(Lookup lkp) {
        Project ret = lkp.lookup(Project.class);
        //Try getting it from Dataobject
        if (ret == null) {
            DataObject dataObject = lkp.lookup(DataObject.class);
            if (dataObject != null) {
                FileObject primary = dataObject.getPrimaryFile();
                ret = FileOwnerQuery.getOwner(primary);
            }
        }
        return ret;
    }

    public static Object castValue(String val, SettingTypes type) {
        switch (type) {
            case BOOLEAN :
                return val.compareTo("true") == 0;
            case INTEGER:
                return Integer.parseInt(val);
        }
        return val;
    }

    public static String castValueToString(Object val, SettingTypes type) {
        switch (type) {
            case BOOLEAN :
                if ((Boolean)val == true) {
                    return "true";
                } else {
                    return "false";
                }
            case INTEGER:
                return "" + ((Integer)val);
        }
        return (String)val;
    }
}
