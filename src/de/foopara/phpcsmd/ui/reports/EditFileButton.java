package de.foopara.phpcsmd.ui.reports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

import de.foopara.phpcsmd.generics.GenericHelper;

public class EditFileButton extends JButton
{

    public String _filename = null;

    public Lookup lkp;

    public EditFileButton(Lookup lkp) {
        super();
        this.lkp = lkp;
        this.setText("Edit File");
        this.addActionListener(new ActionListener()
        {
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
        if (this._filename == null) {
            return;
        }
        FileObject fo = FileUtil.toFileObject(new File(this._filename));
        if (!GenericHelper.isDesirableFile(fo)) {
            return;
        }
        Openable oc = GenericHelper.getFileLookup(fo).lookup(Openable.class);
        if (oc != null) {
            oc.open();
        }
    }

}
