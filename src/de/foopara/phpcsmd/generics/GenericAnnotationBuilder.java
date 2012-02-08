/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.FileListenerRegistry;
import de.foopara.phpcsmd.ViolationRegistry;
import java.util.List;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class GenericAnnotationBuilder {

    public static void updateAnnotations(FileObject fo) {
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpcs(fo));
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpmd(fo));
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpcpd(fo));
    }

    public static void run(FileObject fo, GenericResult res) {
        if (!GenericHelper.isDesirableFile(fo)) return;

        FileListenerRegistry.getListener(fo);

        try {
            DataObject oData = DataObject.find(fo);
            LineCookie cookie = oData.getCookie(LineCookie.class);

            GenericAnnotationBuilder.annotateList(
                    res.getWarnings(),
                    cookie
            );
            GenericAnnotationBuilder.annotateList(
                    res.getErrors(),
                    cookie
            );
            GenericAnnotationBuilder.annotateList(
                    res.getNoTask(),
                    cookie
            );
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static void annotateList(List<GenericViolation> list, LineCookie cookie) {
        for (int i = 0; i < list.size(); i++) {
            GenericAnnotationBuilder.annotateLine(
                    list.get(i),
                    cookie);
        }
    }

    private static void annotateLine(GenericViolation v, LineCookie cookie) {
        System.out.println("Annotate Line");
        for (int i = v.getBeginLine(); i <= v.getEndLine(); i++) {
            System.out.println(v.getShortDescription());
            Line.Set lineSet = cookie.getLineSet();
            Line line = lineSet.getOriginal(i);
            v.getViolationForLine(i).attach(line);
        }
    }
}