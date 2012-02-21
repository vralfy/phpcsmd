/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

@ActionID(id = "de.foopara.phpcsmd.PHPCSMDScanner", category = "PHP")
@ActionRegistration(displayName = "#CTL_PHPCSMDScanner", iconBase = "de/foopara/phpcsmd/resources/icon.png")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 876)
})
@Messages("CTL_PHPCSMDScanner=Scan for violations")
public final class PHPCSMDScanner implements ActionListener {

    private final DataObject context;

    public PHPCSMDScanner(DataObject context) {
        this.context = context;
    }

    private void performOnFileObject(FileObject fo) {
        if (fo.isFolder()) {
            ScanReportTopComponent form = new ScanReportTopComponent();
            form.setFileObject(fo);
            form.open();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fo = this.context.getPrimaryFile();
        this.performOnFileObject(fo);
        //throw new UnsupportedOperationException("Scanning directory " + this.context.getPrimaryFile().getPath() + " not supported yes");
    }

//    @Override
//    protected void performAction(Node[] nodes) {
//        if(nodes.length != 1) {
//            return;
//        }
//        FileObject fo = getFileObject(nodes[0]);
//        this.performOnFileObject(fo);
//    }
//
//    @Override
//    protected boolean enable(Node[] nodes) {
//        return true;
//    }
//
//    @Override
//    public String getName() {
//        return "Scan for violations";
//    }
//
//    @Override
//    public HelpCtx getHelpCtx() {
//        return HelpCtx.DEFAULT_HELP;
//    }

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
