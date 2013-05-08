package de.foopara.phpcsmd;

import javax.swing.JMenuItem;

import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

import de.foopara.phpcsmd.generics.GenericExecute;

@ActionID(id = "de.foopara.phpcsmd.PHPCSMD", category = "PHP")
@ActionRegistration(displayName = "#CTL_PHPCSMD", iconBase = "de/foopara/phpcsmd/resources/icon.png")
@ActionReferences({
    @ActionReference(path = "Editors/text/x-php5/Popup", position = 875),
    @ActionReference(path = "Loaders/text/x-php5/Actions", position = 875)
})
@NbBundle.Messages("CTL_PHPCSMD=Check for violations in file")
public class PHPCSMD extends CookieAction
{

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{Project.class, EditorCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        if (nodes.length != 1) {
            return;
        }
        FileObject fo = getFileObject(nodes[0]);

        GenericExecute.executeQATools(fo);
    }

    @Override
    public String getName() {
        return "Check for violations";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    protected void initialize() {
        super.initialize();// see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return this.setEnabledForExistingBinary(super.getPopupPresenter());
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return this.setEnabledForExistingBinary(super.getMenuPresenter());
    }

    protected JMenuItem setEnabledForExistingBinary(JMenuItem item) {
        item.setEnabled(true);
        return item;
    }

    private FileObject getFileObject(Node node) {
        assert node != null;

        FileObject fileObj = node.getLookup().lookup(FileObject.class);
        if (fileObj != null && fileObj.isValid()) {
            return fileObj;
        }
        DataObject dataObj = node.getCookie(DataObject.class);
        if (dataObj == null) {
            return null;
        }
        fileObj = dataObj.getPrimaryFile();
        if (fileObj != null && fileObj.isValid()) {
            return fileObj;
        }
        return null;
    }

}
