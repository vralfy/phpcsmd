/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.option.GeneralOptions;
import java.io.File;
import java.util.regex.Pattern;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author n.specht
 */
public class GenericHelper {
    public static boolean isDesirableFile(FileObject fileObject) {
        if (fileObject == null) return false;

        return GenericHelper.isDesirableFile(FileUtil.toFile(fileObject));

    }

    public static boolean isDesirableFile(File file) {
        if (file == null) return false;

        if (!file.exists())  return false;
        if (!file.canRead()) return false;
        if (!file.isFile())  return false;

        if (GenericHelper.shouldBeIgnored(file)) return false;

        File parent = new File(file.getParent());
        if (!GenericHelper.isDesirableFolder(parent)) return false;

        return true;
    }

    public static boolean isDesirableFolder(FileObject fileObject) {
        if (fileObject == null) return false;
        return GenericHelper.isDesirableFolder(FileUtil.toFile(fileObject));
    }

    public static boolean isDesirableFolder(File file) {
        if (file == null) return false;

        if (!file.exists())      return false;
        if (!file.canRead())     return false;
        if (!file.isDirectory()) return false;

        if (GenericHelper.shouldBeIgnored(file)) return false;

        return true;
    }

    public static String implode(String glue, String[] arr) {
        StringBuilder tmp = new StringBuilder();
        int pos = 0;
        for(int i=0; i < arr.length; i++) {
            if (pos > 0) tmp.append(glue);
            if (arr[i].trim().length() > 0) {
                tmp.append(arr[i]);
                pos++;
            }
        }
        return tmp.toString();
    }

    private static boolean shouldBeIgnored(FileObject fileObject) {
        if (fileObject == null) return true;
        return GenericHelper.shouldBeIgnored(FileUtil.toFile(fileObject));
    }

    private static boolean shouldBeIgnored(File file) {
        if (file == null) return true;

        String pattern = GeneralOptions.getIgnorePattern();
        if (pattern.trim().length() > 0) {
            if (Pattern.matches(".*(" + pattern + ").*", file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }


}