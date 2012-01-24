/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.io.File;
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

        return true;
    }

}