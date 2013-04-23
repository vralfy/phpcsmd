/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option;

import de.foopara.phpcsmd.PHPCSMD;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author nspecht
 */
abstract public class GenericOption
{

    public static enum SettingTypes
    {

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
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(config);
            p.load(fis);
        } catch (FileNotFoundException ex) {
            Logger.getInstance().log(ex);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
        }
        String ret = p.getProperty(id, def);
//        if (ret.trim().length() == 0) {
//            return def;
//        }
        return ret;
    }

    protected static void setProject(String id, String value, Lookup lkp) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        Properties p = new Properties();
        try {
            File config = GenericOption.getProjectProperties(lkp);
            if (config != null) {
                fis = new FileInputStream(config);
                p.load(fis);
                fis.close();
                fis = null;

                p.setProperty(id, value);
                fos = new FileOutputStream(config);
                p.store(fos, "Phpcsmd options");
            }
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
        }
    }

    protected static void flushProject(String id, Lookup lkp) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Properties p = new Properties();
        try {
            File config = GenericOption.getProjectProperties(lkp);
            if (config != null) {
                fis = new FileInputStream(config);
                p.load(fis);
                fis.close();
                fis = null;

                p.remove(id);
                fos = new FileOutputStream(config);
                p.store(fos, "Phpcsmd options");
            }
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
        }
    }

    protected static Boolean isInProject(String id, Lookup lkp) {
        Boolean ret = false;
        FileInputStream fis = null;
        try {
            File config = GenericOption.getProjectProperties(lkp);
            if (config != null) {
                Properties p = new Properties();
                fis = new FileInputStream(config);
                p.load(fis);
                if (p.containsKey(id)) {
                    ret = true;
                }
            }
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getInstance().log(ex);
                }
            }
        }
        return ret;
    }

    protected static File getProjectProperties(Lookup lkp) {
        if (lkp == null) {
            // OK it's null, take it like a man and move on
            Logger.getInstance().log(new Exception("Lookup is null."), Logger.Severity.USELESS);
            return null;
        }
        Project project = GenericHelper.getProjectFromLookup(lkp);
        if (project == null) {
            // You aren't able to find the project lookup? That's sad!
            Logger.getInstance().log(new Exception("Project was not found in lookup."), Logger.Severity.USELESS);
            return null;
        }

        FileObject fo = project.getProjectDirectory();

        File config = new File(FileUtil.toFile(fo), "nbproject/private/phpcsmd.xml");
        if (!config.exists()) {
            try {
                if (!config.getParentFile().exists()) {
                    config.getParentFile().mkdirs();
                }
                boolean created = config.createNewFile();
                if (!created) {
                    Logger.getInstance().logPre("failed to create custom settings file in " + config.getAbsolutePath(), "phpcsmd");
                } else {
                    config.setReadable(true);
                    config.setWritable(true);
                }
            } catch (IOException ex) {
                Logger.getInstance().log(ex);
            }
        }
        return config;
    }

    public static Object castValue(String val, SettingTypes type) {
        switch (type) {
            case BOOLEAN:
                return val.compareTo("true") == 0;
            case INTEGER:
                return Integer.parseInt(val);
            default:
                return val.toString();
        }
    }

    public static String castValueToString(Object val, SettingTypes type) {
        switch (type) {
            case BOOLEAN:
                if ((Boolean)val == true) {
                    return "true";
                } else {
                    return "false";
                }
            case INTEGER:
                return "" + ((Integer)val);
            default:
                return val.toString();
        }
    }

}
