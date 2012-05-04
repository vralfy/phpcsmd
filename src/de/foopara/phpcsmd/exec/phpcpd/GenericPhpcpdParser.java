package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.File;
import java.util.List;

/**
 *
 * @author nspecht
 */
public class GenericPhpcpdParser {
    protected void add(
            String fo,
            List<GenericViolation> cpdErrors,
            List<GenericViolation> cpdNoTask,
            String f1, int s1, int e1,
            String f2, int s2, int e2) {
        if (!GenericHelper.isDesirableFile(new File(f1)) || !GenericHelper.isDesirableFile(new File(f2))) {
            return;
        }
        if ((f1.compareTo(f2) != 0 && f2.compareTo(fo) == 0)
            || (f1.compareTo(f2) == 0 && s1 > s2)
        ) {
            String tf = f1;
            int ts = s1;
            int te = e1;

            f1 = f2;
            s1 = s2;
            e1 = e2;

            f2 = tf;
            s2 = ts;
            e2 = te;
        }

        String fpath = "";
        if (f1.compareTo(f2) != 0) {
            fpath = " " + f2;
        }
        if (fo.compareTo(f1) == 0) {
            cpdErrors.add(new GenericViolation("Duplicated Sourcecode" + fpath + ": " + (s2+1) + "-" + (e2+1), s1, e1)
                    .setAnnotationType("phpcpd-violation"));
        }
        if (f1.compareTo(f2) != 0) {
            fpath = " " + f1;
        }
        System.err.println(fo + " ==" + f2);
        if (cpdNoTask != null && fo.compareTo(f2) == 0) {
            cpdNoTask.add(new GenericViolation("Duplicated Sourcecode" + fpath + ": " + (s1+1) + "-" + (e1+1), s2, e2)
                    .setAnnotationType("phpcpd-violation"));
        }
    }
}
