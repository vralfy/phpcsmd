package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.option.GeneralOptions;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;

public class GenericHelper
{

    private static File phpcpdDistractor = null;

    public static boolean isDesirableFile(FileObject fileObject) {
        return GenericHelper.isDesirableFile(fileObject, true);
    }

    public static boolean isDesirableFile(FileObject fileObject, boolean filter) {
        if (fileObject == null) {
            return false;
        }
        return GenericHelper.isDesirableFile(FileUtil.toFile(fileObject), filter, GenericHelper.getFileLookup(fileObject));
    }

    public static boolean isDesirableFile(File file, Lookup lkp) {
        return GenericHelper.isDesirableFile(file, true, lkp);
    }

    public static boolean isDesirableFile(File file, boolean filter, Lookup lkp) {
        if (file == null) {
            return false;
        }

        if (!file.exists()) {
            return false;
        }
        if (!file.canRead()) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }

        if (filter && GenericHelper.shouldBeIgnored(file, lkp)) {
            return false;
        }

        File parent = new File(file.getParent());
        if (!GenericHelper.isDesirableFolder(parent, filter, lkp)) {
            return false;
        }

        return true;
    }

    public static boolean isDesirableFolder(FileObject fileObject) {
        return GenericHelper.isDesirableFolder(fileObject, true);
    }

    public static boolean isDesirableFolder(FileObject fileObject, boolean filter) {
        if (fileObject == null) {
            return false;
        }
        return GenericHelper.isDesirableFolder(FileUtil.toFile(fileObject), filter, GenericHelper.getFileLookup(fileObject));
    }

    public static boolean isDesirableFolder(File file, Lookup lkp) {
        return GenericHelper.isDesirableFolder(file, true, lkp);
    }

    public static boolean isDesirableFolder(File file, boolean filter, Lookup lkp) {
        if (file == null) {
            return false;
        }

        if (!file.exists()) {
            return false;
        }
        if (!file.canRead()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }

        if (filter && GenericHelper.shouldBeIgnored(file, lkp)) {
            return false;
        }

        return true;
    }

    public static String implode(String glue, String[] arr) {
        StringBuilder tmp = new StringBuilder();
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            if (pos > 0) {
                tmp.append(glue);
            }
            if (arr[i].trim().length() > 0) {
                tmp.append(arr[i]);
                pos++;
            }
        }
        return tmp.toString();
    }

    private static boolean shouldBeIgnored(FileObject fileObject) {
        if (fileObject == null) {
            return true;
        }
        return GenericHelper.shouldBeIgnored(FileUtil.toFile(fileObject), GenericHelper.getFileLookup(fileObject));
    }

    private static boolean shouldBeIgnored(File file, Lookup lkp) {
        if (file == null) {
            return true;
        }

        String pattern = (String)GeneralOptions.load(GeneralOptions.Settings.IGNORE, lkp);
        if (pattern.trim().length() > 0) {
            if (Pattern.matches(".*(" + pattern + ").*", file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    public static String getPhpcpdDistractor() {
        if (GenericHelper.phpcpdDistractor == null) {
            try {
                GenericHelper.phpcpdDistractor = File.createTempFile("phpcsmd-phpcpdDistractor", ".php");
            } catch (IOException ex) {
                Logger.getInstance().log(ex);
                GenericHelper.phpcpdDistractor = new File("phpcsmd-phpcpdDistractor.php");
            }
        }
        return GenericHelper.phpcpdDistractor.getAbsolutePath();
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    public static String escapePath(FileObject f) {
        String escapedFilename = f.getPath();
        if (escapedFilename.contains(" ")) {
            escapedFilename = escapedFilename.replaceAll(" ", "\\\\ ");
        }
        return escapedFilename;
    }

    public static Lookup getFileLookup(FileObject fo) {
        Lookup ret = null;
        try {
            ret = DataObject.find(fo).getLookup();
        } catch (DataObjectNotFoundException ex) {
            Logger.getInstance().log(ex);
        }

        if (ret == null) {
            try {
                ret = fo.getLookup();
            } catch (NoSuchMethodError ex) {
                Logger.getInstance().log("NoSuchMethodError: " + ex.getMessage());
            } catch (Exception ex) {
                Logger.getInstance().log(ex);
            }
        }

        return ret;
    }

}