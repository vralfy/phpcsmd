/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.exec.codesniffer.Codesniffer;
import javax.swing.JMenuItem;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;

public class PHPCSMD extends CookieAction {

    private Codesniffer codesniffer = Codesniffer.getInstance();

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{EditorCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        if(nodes.length != 1) {
            return;
        }
        FileObject fo = getFileObject(nodes[0]);
    }

    @Override
    public String getName() {
        return "CTL_PHPCSMD_Action";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected void initialize() {
        super.initialize();// see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public JMenuItem getPopupPresenter()
    {
        return this.setEnabledForExistingBinary(super.getPopupPresenter());
    }

    @Override
    public JMenuItem getMenuPresenter()
    {
        return this.setEnabledForExistingBinary(super.getMenuPresenter());
    }

    protected JMenuItem setEnabledForExistingBinary(JMenuItem item)
    {
        item.setEnabled(this.codesniffer.isEnabled());
        return item;
    }

    private FileObject getFileObject(Node node)
    {
        assert node != null;

        FileObject fileObj = node.getLookup().lookup(FileObject.class);
        if (fileObj != null && fileObj.isValid()) {
            return fileObj;
        }
        DataObject dataObj = node.getCookie(DataObject.class);
        if (dataObj == null) {
            return null;
        }
        fileObj = (FileObject) dataObj.getPrimaryFile();
        if (fileObj != null && fileObj.isValid()) {
            return fileObj;
        }
        return null;
    }
}
