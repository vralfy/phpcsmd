package de.foopara.phpcsmd.generics;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.option.GeneralOptions;

public class GenericHelper
{

    private static volatile File phpcpdDistractor = null;

    public static boolean isDesirableFile(FileObject fileObject) {
        return GenericHelper.isDesirableFile(fileObject, true);
    }

    public static boolean isDesirableFile(FileObject fileObject, boolean filter) {
        if (fileObject == null) {
            return false;
        }
        return GenericHelper.isDesirableFile(FileUtil.toFile(fileObject), GenericHelper.getFileLookup(fileObject), filter);
    }

    public static boolean isDesirableFile(File file, Lookup lkp, boolean filter) {
        if (file == null) {
            Logger.getInstance().log("is not desired (null)", "", Logger.Severity.USELESS);
            return false;
        }

        if (!file.exists()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (does not exist)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }
        if (!file.canRead()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (can't read)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }
        if (!file.isFile()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (not a file)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }

        if (filter && GenericHelper.shouldBeIgnored(FileUtil.toFileObject(file))) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (on ignore list)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }

        Project project = GenericHelper.getProjectFromLookup(lkp);
        if ((project == null
            || !project.getClass().getCanonicalName().endsWith("php.project.PhpProject"))
            && (Boolean)GeneralOptions.loadOriginal(GeneralOptions.Settings.SCANINNONPHP) == false
        ) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (non PHP project)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }

        File parent = new File(file.getParent());
        if (!GenericHelper.isDesirableFolder(parent, lkp, filter)) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (parent folder is not desired)", "desirableFile", Logger.Severity.USELESS);
            return false;
        }

        return true;
    }

    public static boolean isDesirableFolder(FileObject fileObject) {
        return GenericHelper.isDesirableFolder(FileUtil.toFile(fileObject), GenericHelper.getFileLookup(fileObject), true);
    }

    public static boolean isDesirableFolder(File file, Lookup lkp, boolean filter) {
        if (file == null) {
            Logger.getInstance().log("is not desired (null)", "desirableFolder", Logger.Severity.USELESS);
            return false;
        }

        if (!file.exists()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (does not exist)", "desirableFolder", Logger.Severity.USELESS);
            return false;
        }
        if (!file.canRead()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (is not readable)", "desirableFolder", Logger.Severity.USELESS);
            return false;
        }
        if (!file.isDirectory()) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (no such directory)", "desirableFolder", Logger.Severity.USELESS);
            return false;
        }

        if (filter && GenericHelper.shouldBeIgnored(file, lkp)) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (ignored)", "desirableFolder", Logger.Severity.USELESS);
            return false;
        }

        Project project = GenericHelper.getProjectFromLookup(lkp);
        if ((project == null
            || !project.getClass().getCanonicalName().endsWith("php.project.PhpProject"))
            && (Boolean)GeneralOptions.loadOriginal(GeneralOptions.Settings.SCANINNONPHP) == false
        ) {
            Logger.getInstance().log(file.getAbsolutePath() + " is not desired (non PHP project)", "desirable folder", Logger.Severity.USELESS);
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
        if (file == null || lkp == null) {
            return true;
        }

        boolean retMatched;
        String pattern;

        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.INCLUDESTRATEGY, lkp)) {
            if (file.isDirectory()) {
                return false; // folders can never match the regex so they have to be parsed
            }
            retMatched = false;
            pattern = (String)GeneralOptions.load(GeneralOptions.Settings.INCLUDE, lkp);
        } else {
            retMatched = true;
            pattern = (String)GeneralOptions.load(GeneralOptions.Settings.IGNORE, lkp);
        }

        if (pattern.trim().length() > 0) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(file.getAbsolutePath());
            boolean found = m.find();

            if (found) {
                Logger.getInstance().logPre(file.getAbsolutePath() + " matches " + pattern + " " + retMatched, "ignore", Logger.Severity.EXCEPTION);
                return retMatched;
            }
        }

        return !retMatched;
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

    public static Project getProjectFromLookup(Lookup lkp) {
        if (lkp == null) {
            return null;
        }

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

    public static Lookup getFileLookup(FileObject fo) {
        try {
            if (fo == null
                || fo.isVirtual()
                || DataObject.find(fo).getLookup() == null
                || !(
                    GenericHelper.isDesirableFile(FileUtil.toFile(fo), DataObject.find(fo).getLookup(), false)
                    || GenericHelper.isDesirableFolder(FileUtil.toFile(fo), DataObject.find(fo).getLookup(), false)
                    )
            ) {
                if (fo != null) {
                    Logger.getInstance().logPre("Can not find lookup for " + fo.getPath(), "", Logger.Severity.USELESS);
                }
                return null;
            }
        } catch (Exception ex) {
            if (ex instanceof IOException) {
                Logger.getInstance().log(ex, Logger.Severity.USELESS);
            } else {
                Logger.getInstance().log(ex);
            }
        }

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