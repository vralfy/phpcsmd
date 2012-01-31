/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcpd;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class PhpcpdParser {

    public PhpcpdResult parse(GenericOutputReader reader) {
        List<GenericViolation> cpdErrors = new ArrayList<GenericViolation>();
        List<GenericViolation> cpdNoTask = new ArrayList<GenericViolation>();

        try {
            char[] tmp = new char[1024];
            StringBuilder buf = new StringBuilder();
            Reader r = reader.getReader();
            while (r.read(tmp) > 0) {
                buf.append(tmp);
            }

            String[] sections = buf.toString().trim().split("\n\n");
            if (sections.length < 3) {
                return new PhpcpdResult(null, null, null);
            }
            if (!sections[1].trim().startsWith("Found ")) {
                return new PhpcpdResult(null, null, null);
            }
            for (int i=2; i < sections.length - 2; i++) {
                String[] lines = sections[i].split("\n");
                String[] info1 = lines[0].trim().split(":");
                String[] cpdLines1 = info1[info1.length - 1].split("-");
                String[] info2 = lines[1].trim().split(":");
                String[] cpdLines2 = info2[info2.length - 1].split("-");
                System.out.println(info1[info1.length - 1] + " " + info2[info2.length - 1]);
                int start1 = Integer.parseInt(cpdLines1[0]);
                int start2 = Integer.parseInt(cpdLines2[0]);
                int end1 = Integer.parseInt(cpdLines1[1]);
                int end2 = Integer.parseInt(cpdLines2[1]);

                cpdErrors.add(new GenericViolation("Duplicated Sourcecode (" + start2 + "-" + end2 + ")", start1, end1)
                        .setAnnotationType("phpcpd-violation"));
                cpdNoTask.add(new GenericViolation("Duplicated Sourcecode (" + start1 + "-" + end1 + ")", start2, end2)
                        .setAnnotationType("phpcpd-violation"));
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new PhpcpdResult(null, cpdErrors, cpdNoTask);
    }
}
