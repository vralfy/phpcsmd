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
import org.openide.util.NbBundle.Messages;

@ActionID(id = "de.foopara.phpcsmd.PHPCSMDScanner", category = "PHP")
@ActionRegistration(displayName = "#CTL_PHPCSMDScanner", iconBase = "de/foopara/phpcsmd/resources/icon.png")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 876),
    //@ActionReference(path = "Loaders/text/x-php5/Actions", position = 876),
    //@ActionReference(path = "Projects/Actions", position = 876),
    @ActionReference(path = "UI/ToolActions/PHP", position = 876)
})
@Messages("CTL_PHPCSMDScanner=Scan for violations")
public final class PHPCSMDScanner implements ActionListener {

    private final DataObject context;

    public PHPCSMDScanner(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fo = this.context.getPrimaryFile();
        this.performOnFileObject(fo);
    }

    private void performOnFileObject(FileObject fo) {
        if (fo.isFolder()) {
            ScanReportTopComponent form = new ScanReportTopComponent();
            form.setFileObject(fo);
            form.setLookup(this.context.getLookup());
            form.open();
        }
    }
}
