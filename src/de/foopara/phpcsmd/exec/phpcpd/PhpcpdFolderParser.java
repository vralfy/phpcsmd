package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericHelper;
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
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class PhpcpdFolderParser extends GenericPhpcpdParser
{

    public HashMap<String, PhpcpdResult> parse(GenericOutputReader reader, FileObject containingFolder) {
        HashMap<String, List<GenericViolation>> cpdErrors = new HashMap<String, List<GenericViolation>>();
        HashMap<String, List<GenericViolation>> cpdNoTask = new HashMap<String, List<GenericViolation>>();
        Lookup lookup = GenericHelper.getFileLookup(containingFolder);
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

            for (int i = 2; i < sections.length - 2; i++) {
                if (this.isValidPhpcpdSection(sections[i])) {
                    PhpcpdLine line = new PhpcpdLine(sections[i]);
                    Logger.getInstance().logPre(line.toString(), "phpcpd");
                    if (!cpdErrors.containsKey(line.file1)) {
                        cpdErrors.put(line.file1, new ArrayList<GenericViolation>());
                    }
                    if (!cpdErrors.containsKey(line.file2)) {
                        cpdErrors.put(line.file2, new ArrayList<GenericViolation>());
                    }
                    if (!cpdNoTask.containsKey(line.file1)) {
                        cpdNoTask.put(line.file1, new ArrayList<GenericViolation>());
                    }
                    if (!cpdNoTask.containsKey(line.file2)) {
                        cpdNoTask.put(line.file2, new ArrayList<GenericViolation>());
                    }

                    this.add(line.file1, lookup, cpdErrors.get(line.file1), cpdNoTask.get(line.file1),
                            line.file1, line.start1, line.end1,
                            line.file2, line.start2, line.end2);
                    if (line.file1.compareTo(line.file2) != 0) {
                        this.add(line.file2, lookup, cpdErrors.get(line.file2), cpdNoTask.get(line.file2),
                                line.file1, line.start1, line.end1,
                                line.file2, line.start2, line.end2);
                    }

                    FileObject tmpf1 = FileUtil.toFileObject(new File(line.file1));
                    FileObject tmpf2 = FileUtil.toFileObject(new File(line.file2));
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
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }

        HashMap<String, PhpcpdResult> ret = new HashMap<String, PhpcpdResult>();
        for (String key : cpdErrors.keySet()) {
            ret.put(key, new PhpcpdResult(null, cpdErrors.get(key), cpdNoTask.get(key)));
        }
        return ret;
    }

}
