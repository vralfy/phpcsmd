package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.ui.reports.PdependReportTopComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@ActionID(id = "de.foopara.phpcsmd.PdependScanner", category = "PHP")
@ActionRegistration(displayName = "#CTL_PHPdependScanner", iconBase = "de/foopara/phpcsmd/resources/icon.png")
@ActionReferences({
    @ActionReference(path = "Loaders/folder/any/Actions", position = 877),
    @ActionReference(path = "Loaders/text/x-php5/Actions", position = 877),
//    @ActionReference(path = "Projects/Actions", position = 877),
    @ActionReference(path = "UI/ToolActions/PHP", position = 877)
})
@Messages("CTL_PHPdependScanner=Scan with Pdepend")
public final class PdependScanner implements ActionListener {

    private final DataObject context;

    public PdependScanner(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject fo = this.context.getPrimaryFile();
        this.performOnFileObject(fo);
    }

    private void performOnFileObject(FileObject fo) {
        if (fo.isFolder() || fo.isData()) {
            PdependReportTopComponent form = new PdependReportTopComponent();
            form.setLookup(GenericHelper.getFileLookup(fo));
            form.setFileObject(fo);
            form.open();
        }
    }
}
