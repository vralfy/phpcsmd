/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.threads.QAThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author nspecht
 */
@ActionID(id = "de.foopara.phpcsmd.ListenerToggleDisplay", category = "PHP")
@ActionRegistration(displayName = "#CTL_ListenerToggleDisplay", iconBase = "de/foopara/phpcsmd/resources/icon.png")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 877),
    @ActionReference(path = "Loaders/text/x-php5/Actions", position = 877),
//    @ActionReference(path = "Projects/Actions", position = 877),
    @ActionReference(path = "UI/ToolActions/PHP", position = 877)
})
@NbBundle.Messages("CTL_ListenerToggleDisplay=Toggle phpcsmd annotations")
public class ListenerToggleDisplay implements ActionListener {

    public static boolean showAnnotations = true;

    @Override
    public void actionPerformed(ActionEvent e) {
        ListenerToggleDisplay.showAnnotations = !ListenerToggleDisplay.showAnnotations;

        Set<TopComponent> topComponents = TopComponent.getRegistry().getOpened();
        for (TopComponent topComponent : topComponents) {
            if (topComponent != null && topComponent.getLookup() != null) {
//                Node[] nodes = topComponent.getActivatedNodes();
//                for (Node node : nodes) {
//                    if (node.getCookie(EditorCookie.class) != null) {
                        DataObject dObj = topComponent.getLookup().lookup(DataObject.class);
                        if (dObj != null) {
                            FileObject file = dObj.getPrimaryFile();
                            if (file != null) {
                                GenericAnnotationBuilder.updateAnnotations(file);
//                                ViolationRegistry.getInstance().reprintTasks(file);
                            }
                        }
                    }
//                }
//            }
        }
    }

}
