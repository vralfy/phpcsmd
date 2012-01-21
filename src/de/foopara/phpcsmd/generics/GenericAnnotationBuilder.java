/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

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
    public static void run(FileObject fo, GenericResult res) {
        GenericFileListener listener = new GenericFileListener();
        listener.setResult(res);
        fo.addFileChangeListener(listener);
        
        try {
            DataObject oData = DataObject.find(fo);
            LineCookie cookie = oData.getCookie(LineCookie.class);

            Line.Set lineSet = null;
            Line line = null;
            for (int i = 0; i < res.getWarnings().size(); i++) {
                lineSet = cookie.getLineSet();
                line = lineSet.getOriginal(res.getWarnings().get(i).getLine());
                res.getWarnings().get(i).attach(line);
            }
            for (int i = 0; i < res.getErrors().size(); i++) {
                lineSet = cookie.getLineSet();
                line = lineSet.getOriginal(res.getErrors().get(i).getLine());
                res.getErrors().get(i).attach(line);
            }
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
