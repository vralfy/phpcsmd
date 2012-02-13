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

        File parent = new File(file.getParent());
        if (!parent.exists())      return false;
        if (!parent.canRead())     return false;
        if (!parent.isDirectory()) return false;

        String pattern = GeneralOptions.getIgnorePattern();
        if (pattern.trim().length() > 0) {
            if (Pattern.matches(".*(" + pattern + ").*", file.getAbsolutePath())) {
                return false;
            }
        }
        
        return true;
    }

}