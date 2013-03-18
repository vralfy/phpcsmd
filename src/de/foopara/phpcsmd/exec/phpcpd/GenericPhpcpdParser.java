package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.File;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class GenericPhpcpdParser
{

    public static class PhpcpdLine
    {

        public String file1;

        public int start1;

        public int end1;

        public String file2;

        public int start2;

        public int end2;

        public PhpcpdLine(String section) {
            String[] lines = section.split("\n");

            String[] info1 = lines[0].trim().split(":");
            StringBuilder f1 = new StringBuilder(info1[0].replaceFirst("-", "").trim());
            for (int sectionCounter = 1; sectionCounter < info1.length - 1; sectionCounter++) {
                f1.append(":").append(info1[sectionCounter]);
            }
            String[] cpdLines1 = info1[info1.length - 1].split("-");

            String[] info2 = lines[1].trim().split(":");
            StringBuilder f2 = new StringBuilder(info2[0].trim());
            for (int sectionCounter = 1; sectionCounter < info2.length - 1; sectionCounter++) {
                f2.append(":").append(info2[sectionCounter]);
            }
            String[] cpdLines2 = info2[info2.length - 1].split("-");

            this.file1 = f1.toString();
            this.file2 = f2.toString();

            this.start1 = Integer.parseInt(cpdLines1[0]);
            this.start2 = Integer.parseInt(cpdLines2[0]);
            this.end1 = Integer.parseInt(cpdLines1[1]);
            this.end2 = Integer.parseInt(cpdLines2[1]);
        }

        @Override
        public String toString() {
            return this.file1 + " " + this.start1 + ":" + this.end1 + "\n"
                    + this.file2 + " " + this.start2 + ":" + this.end2 + "\n";
        }

    }

    protected boolean isValidPhpcpdSection(String section) {
        if (!section.contains("duplicated lines out of")
                && !section.contains("Time: ")
                && !section.contains("Memory: ")
                && !section.contains(",")
                && section.contains("\n")
                && section.contains(":")
                && section.contains("-")) {
            return true;
        }
        return false;
    }

    protected void add(
            String fo,
            Lookup lkp,
            List<GenericViolation> cpdErrors,
            List<GenericViolation> cpdNoTask,
            String f1, int s1, int e1,
            String f2, int s2, int e2) {
        if (!GenericHelper.isDesirableFile(new File(f1), lkp)
                || !GenericHelper.isDesirableFile(new File(f2), lkp)) {
            return;
        }
        if ((f1.compareTo(f2) != 0 && f2.compareTo(fo) == 0)
                || (f1.compareTo(f2) == 0 && s1 > s2)) {
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
            cpdErrors.add(new GenericViolation("Duplicated Sourcecode" + fpath + ": " + (s2 + 1) + "-" + (e2 + 1), s1, e1)
                    .setAnnotationType("phpcpd-violation").setGroup("phpcpd-violation"));
        }

        if (f1.compareTo(f2) != 0) {
            fpath = " " + f1;
        }
        if (cpdNoTask != null && fo.compareTo(f2) == 0) {
            cpdNoTask.add(new GenericViolation("Duplicated Sourcecode" + fpath + ": " + (s1 + 1) + "-" + (e1 + 1), s2, e2)
                    .setAnnotationType("phpcpd-violation").setGroup("phpcpd-violation"));
        }
    }

}
