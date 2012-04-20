package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class PhpcpdFolderParser extends GenericPhpcpdParser {

    public HashMap<String, PhpcpdResult> parse(GenericOutputReader reader, FileObject containingFolder) {
        HashMap<String, List<GenericViolation>> cpdErrors = new HashMap<String, List<GenericViolation>>();

        try {
            char[] tmp = new char[1024];
            StringBuilder buf = new StringBuilder();
            Reader r = reader.getReader();
            while (r.read(tmp) > 0) {
                buf.append(tmp);
            }

            String[] sections = buf.toString().trim().split("\n\n");
            if (sections.length < 3) {
                return new HashMap<String, PhpcpdResult>();
            }
            if (!sections[1].trim().startsWith("Found ")) {
                return new HashMap<String, PhpcpdResult>();
            }

            ArrayList<FileObject> allreadyFlushedDependencies = new ArrayList<FileObject>();

            for (int i=2; i < sections.length - 2; i++) {
                if (!sections[i].contains("duplicated lines out of")
                    && !sections[i].contains("Time: ")
                    && !sections[i].contains("Memory: ")
                    && !sections[i].contains(",")
                    && sections[i].contains("\n")
                    && sections[i].contains(":")
                    && sections[i].contains("-")
                ) {
                    String[] lines = sections[i].split("\n");

                    String[] info1 = lines[0].trim().split(":");
                    String f1 = info1[0].replaceFirst("-", "").trim();
                    String[] cpdLines1 = info1[info1.length - 1].split("-");

                    String[] info2 = lines[1].trim().split(":");
                    String f2 = info2[0].trim();
                    String[] cpdLines2 = info2[info2.length - 1].split("-");
                    int start1 = Integer.parseInt(cpdLines1[0]);
                    int start2 = Integer.parseInt(cpdLines2[0]);
                    int end1 = Integer.parseInt(cpdLines1[1]);
                    int end2 = Integer.parseInt(cpdLines2[1]);

                    if (!cpdErrors.containsKey(f1)) {
                        cpdErrors.put(f1, new ArrayList<GenericViolation>());
                    }
                    if (!cpdErrors.containsKey(f2)) {
                        cpdErrors.put(f2, new ArrayList<GenericViolation>());
                    }

                    this.add(f1, cpdErrors.get(f1), null, f1, start1, end1, f2, start2, end2);
                    this.add(f2, cpdErrors.get(f2), null, f1, start1, end1, f2, start2, end2);

                    FileObject tmpf1 = FileUtil.toFileObject(new File(f1));
                    FileObject tmpf2 = FileUtil.toFileObject(new File(f2));
                    if (tmpf1 != null && tmpf2 != null && tmpf1.getPath().compareTo(tmpf2.getPath()) != 0) {
                        if (!allreadyFlushedDependencies.contains(tmpf1)) {
                            ViolationRegistry.getInstance().flushPhpcpdDependency(tmpf1);
                            allreadyFlushedDependencies.add(tmpf1);
                        }
                        if (!allreadyFlushedDependencies.contains(tmpf2)) {
                            ViolationRegistry.getInstance().flushPhpcpdDependency(tmpf2);
                            allreadyFlushedDependencies.add(tmpf2);
                        }
                        ViolationRegistry.getInstance().addPhpcpdDependency(tmpf1, tmpf2);
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        HashMap<String, PhpcpdResult> ret = new HashMap<String, PhpcpdResult>();
        for (String key : cpdErrors.keySet()) {
            ret.put(key, new PhpcpdResult(null, cpdErrors.get(key), null));
        }
        return ret;
    }
}
