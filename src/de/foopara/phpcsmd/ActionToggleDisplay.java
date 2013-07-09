/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericAnnotationBuilder;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.JToggleButton;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.Actions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.BooleanStateAction;
import org.openide.windows.TopComponent;

@ActionID(
        category = "PHP",
        id = "de.foopara.phpcsmd.ActionToggleDisplay"
        )
@ActionRegistration(
        iconBase = "de/foopara/phpcsmd/resources/inactive.png",
        displayName = "#CTL_ActionToggleDisplay"
        )
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 878),
    @ActionReference(path = "Loaders/text/x-php5/Actions", position = 878),
//    @ActionReference(path = "Projects/Actions", position = 877),
    @ActionReference(path = "UI/ToolActions/PHP", position = 878)
})
@Messages("CTL_ActionToggleDisplay=Show phpcsmd annotations")
public final class ActionToggleDisplay extends BooleanStateAction {

    public static boolean showAnnotations = true;

    private JToggleButton jToggleButton = null;

    public ActionToggleDisplay() {
        this.setBooleanState(true);
        this.jToggleButton = new JToggleButton();
        Actions.connect(this.jToggleButton, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        ActionToggleDisplay.showAnnotations = this.getBooleanState();

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

    @Override
    public String getName() {
        return "Show phpcsmd annotations";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String iconResource() {
//        if (this.getBooleanState()) {
            return "de/foopara/phpcsmd/resources/active.png";
//        }
//        return "de/foopara/phpcsmd/resources/inactive.png";
    }

    @Override
    public Component getToolbarPresenter() {
        return this.jToggleButton;
    }
}
