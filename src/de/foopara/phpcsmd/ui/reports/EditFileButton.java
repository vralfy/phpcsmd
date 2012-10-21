package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class EditFileButton extends JButton {
    public String _filename = null;

    public Lookup lkp;

    public EditFileButton(Lookup lkp) {
        super();
        this.lkp = lkp;
        this.setText("Edit File");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                openFile();
            }
        });
    }

    public void setFilename(String filename) {
        this._filename = filename;
    }

    private void openFile() {
        if (this._filename == null) return;
        try {
            FileObject fo = FileUtil.toFileObject(new File(this._filename));
            if (!GenericHelper.isDesirableFile(fo, this.lkp)) {
                return;
            }
            DataObject dob = DataObject.find(fo);
            Openable oc = dob.getLookup().lookup(Openable.class);
            if (oc != null) {
                oc.open();
            }
        } catch (DataObjectNotFoundException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }
    }
}
