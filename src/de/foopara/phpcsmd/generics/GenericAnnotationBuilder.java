package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.FileListenerRegistry;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
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
            Logger.getInstance().log(ex);
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
        for (int i = v.getBeginLine(); i <= v.getEndLine(); i++) {
            try {
                Line.Set lineSet = cookie.getLineSet();
                Line line = lineSet.getOriginal(i);
                v.getViolationForLine(i).attach(line);
            } catch (Exception e) {
                Logger.getInstance().log(e);
            }
        }
    }
}