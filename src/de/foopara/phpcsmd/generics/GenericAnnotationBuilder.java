package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.FileListenerRegistry;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.debug.Logger;
import java.util.List;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;
import org.openide.util.Lookup;

/**
 *
 * @author nspecht
 */
public class GenericAnnotationBuilder
{

    public static void updateAnnotations(FileObject fo) {
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpcs(fo));
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpmd(fo));
        GenericAnnotationBuilder.run(fo, ViolationRegistry.getInstance().getPhpcpd(fo));
    }

    public static void run(FileObject fo, GenericResult res) {
        if (!GenericHelper.isDesirableFile(fo)) {
            return;
        }

        FileListenerRegistry.getListener(fo);

//        try {
//The new way
            Lookup lkp = GenericHelper.getFileLookup(fo);
            LineCookie cookie = lkp.lookup(LineCookie.class);
//            DataObject oData = lkp.lookup(DataObject.class);
//The old way
//            DataObject oData = DataObject.find(fo);
//            LineCookie cookie = oData.getLookup().lookup(LineCookie.class);

            GenericAnnotationBuilder.annotateList(
                    res.getWarnings(),
                    cookie);
            GenericAnnotationBuilder.annotateList(
                    res.getErrors(),
                    cookie);
            GenericAnnotationBuilder.annotateList(
                    res.getNoTask(),
                    cookie);
//        } catch (DataObjectNotFoundException ex) {
//            Logger.getInstance().log(ex);
//        }

// Try to modify the node icon
//        try {
//            DataObject dataObject = DataObject.find(fo);
//            if (dataObject != null) {
//                Node n = dataObject.getNodeDelegate();
//                if (n != null) {
//                    ImageIcon overlay = new ImageIcon(PHPCSMD.class.getResource("icon.png"));
//                    if (overlay != null) {
//                        Image original16 = n.getIcon(BeanInfo.ICON_COLOR_16x16);
//                        Image original32 = n.getIcon(BeanInfo.ICON_COLOR_32x32);
//                        original16.getGraphics().drawImage(overlay.getImage(), 0, 0, null);
//                        original32.getGraphics().drawImage(overlay.getImage(), 0, 0, null);
//                    }
//                    n.setShortDescription("scanned");
//                }
//            }
//        } catch (DataObjectNotFoundException ex) {
//            Logger.getInstance().log(ex);
//        }
    }

    private static void annotateList(List<GenericViolation> list, LineCookie cookie) {
        for (int i = 0; i < list.size(); i++) {
            GenericAnnotationBuilder.annotateLine(
                    list.get(i),
                    cookie);
        }
    }

    private static void annotateLine(GenericViolation v, LineCookie cookie) {
        if (cookie == null || cookie.getLineSet().getLines() == null || cookie.getLineSet().getLines().size() < 1) {
            return;
        }

        Integer maxLine = Math.min(cookie.getLineSet().getLines().size() - 1, v.getEndLine());
        for (int i = v.getBeginLine(); i <= maxLine; i++) {
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