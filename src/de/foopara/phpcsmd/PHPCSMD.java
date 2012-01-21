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

    @Override
    protected int mode() {
        DebugLog.put("PHPCSMD::mode");
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class[] cookieClasses() {
        DebugLog.put("PHPCSMD::cookieClasses");
        return new Class[]{EditorCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        DebugLog.put("PHPCSMD::performAction");
        if(nodes.length != 1) {
            return;
        }
        FileObject fo = getFileObject(nodes[0]);
        DebugLog.put("exec Codesniffer on " + fo.toString());
        new Codesniffer().execute(fo);
    }

    @Override
    public String getName() {
        DebugLog.put("PHPCSMD::getName");
        return "Check for violations";
    }

    @Override
    public HelpCtx getHelpCtx() {
        DebugLog.put("PHPCSMD::getHelpCtx");
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected void initialize() {
        super.initialize();// see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        DebugLog.put("PHPCSMD::initialize");
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public JMenuItem getPopupPresenter()
    {
        DebugLog.put("PHPCSMD::getPopupPresenter");
        return this.setEnabledForExistingBinary(super.getPopupPresenter());
    }

    @Override
    public JMenuItem getMenuPresenter()
    {
        DebugLog.put("PHPCSMD::getMenuPresenter");
        return this.setEnabledForExistingBinary(super.getMenuPresenter());
    }

    protected JMenuItem setEnabledForExistingBinary(JMenuItem item)
    {
        DebugLog.put("PHPCSMD::setEnabledForExistingBinary");
        item.setEnabled(true);
        return item;
    }

    private FileObject getFileObject(Node node)
    {
        DebugLog.put("PHPCSMD::getFileObject");
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
