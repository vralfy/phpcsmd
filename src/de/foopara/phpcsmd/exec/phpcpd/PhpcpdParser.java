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
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class PhpcpdParser extends GenericPhpcpdParser
{

    public PhpcpdResult parse(GenericOutputReader reader, boolean updateDependencies, FileObject fo) {
        List<GenericViolation> cpdErrors = new ArrayList<GenericViolation>();
        List<GenericViolation> cpdNoTask = new ArrayList<GenericViolation>();
        Lookup lookup = GenericHelper.getFileLookup(fo);

        try {
            char[] tmp = new char[1024];
            StringBuilder buf = new StringBuilder();
            Reader r = reader.getReader();
            while (r.read(tmp) > 0) {
                buf.append(tmp);
            }
            Logger.getInstance().logPre(buf.toString(), "Phpcpd output");
            String[] sections = buf.toString().replaceAll("\r", "").trim().split("\n\n");

            if (sections.length < 3) {
                return new PhpcpdResult(null, null, null);
            }
            if (!sections[1].trim().startsWith("Found ")) {
                return new PhpcpdResult(null, null, null);
            }
            for (int i = 2; i < sections.length - 2; i++) {
                if (this.isValidPhpcpdSection(sections[i])) {
                    PhpcpdLine line = new PhpcpdLine(sections[i]);

                    this.add(FileUtil.toFile(fo).getPath(), lookup, cpdErrors, cpdNoTask,
                            line.file1, line.start1, line.end1, line.file2, line.start2, line.end2);

                    if (updateDependencies && line.file1.compareTo(line.file2) != 0) {
                        ViolationRegistry.getInstance().addPhpcpdDependency(FileUtil.toFileObject(new File(line.file1)), FileUtil.toFileObject(new File(line.file2)));
                    }
                } else {
                    Logger.getInstance().logPre(sections[i], "malformed cpd violation");
                }
            }
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Logger.getInstance().log(ex);
        }
        return new PhpcpdResult(null, cpdErrors, cpdNoTask);
    }

}
