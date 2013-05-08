/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.generics.GenericViolation;
import de.foopara.phpcsmd.ui.GenericTable;

/**
 *
 * @author nspecht
 */
public class ScanReportDetailTable extends GenericTable
{

    public enum ReportType
    {

        PHPCS, PHPMD, PHPCPD

    };

    private FileObject rootDir = null;

    private ReportType type;

    public ScanReportDetailTable() {
        super();
        this.type = null;
        this.model.addColumn("");
        this.model.addColumn("Description");
        this.model.addColumn("File");
        this.model.addColumn("Line");

        this.getColumnModel().getColumn(0).setMaxWidth(20);
        this.getColumnModel().getColumn(0).setMinWidth(20);
        this.getColumnModel().getColumn(3).setMaxWidth(80);

        for (int i = 1; i <= 3; i++) {
            this.getColumnModel().getColumn(i).setMinWidth(50);
        }

        this.finishTableSettings();

        this.sorter.setComparator(1, new GenericTable.StringComparator());
        this.sorter.setComparator(2, new GenericTable.FilepathComparator());
        this.sorter.setComparator(3, new GenericTable.IntegerComparator());

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    openSelectedFile();
                }
            }
        });
    }

    public void addElement(FileObject fo) {
        if (!fo.getPath().startsWith(this.rootDir.getPath())) {
            return;
        }

        if (this.type == null) {
            return;
        }

        GenericResult result = this.getViolations(fo);

        if (result.getSum() < 1) {
            return;
        }

        String printedName = fo.getPath().replaceFirst(this.rootDir.getPath(), "");
        boolean doubleEntry = false;
        for (int i = 0; i < this.model.getRowCount(); i++) {
            String val = (String)this.model.getValueAt(i, 2);
            if (val.compareTo(printedName) == 0) {
                doubleEntry = true;
                this.model.removeRow(i);
            }
        }

        List<GenericViolation> list = result.getErrors();
        list.addAll(result.getWarnings());
//        list.addAll(result.getNoTask());
        for (GenericViolation violation : list) {
            this.model.addRow(new Object[]{
                "",
                violation.getShortDescription(),
                printedName,
                violation.getBeginLine(),
            });
        }
    }

    public void poke(FileObject fo) {
        if (!fo.getPath().startsWith(this.rootDir.getPath())) {
            return;
        }
        String subpath = fo.getPath().replaceFirst(this.rootDir.getPath(), "");
        for (int i = this.model.getRowCount() - 1; i >= 0; i--) {
            if (subpath.compareTo((String)this.model.getValueAt(i, 2)) == 0) {
                this.model.removeRow(i);
            }
        }

        this.addElement(fo);
    }

    public void openSelectedFile() {
        String path = (String)this.getValueAt(this.getSelectedRow(), 2);
        FileObject fo = FileUtil.toFileObject(new File(this.rootDir.getPath(), path));
        if (!GenericHelper.isDesirableFile(fo)) {
            return;
        }

        Openable oc = GenericHelper.getFileLookup(fo).lookup(Openable.class);
        if (oc != null) {
            oc.open();
        }
    }

    public void setRootDirectory(FileObject dir) {
        this.rootDir = dir;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    private GenericResult getViolations(FileObject fo) {
        switch (this.type) {
            case PHPCS:
                return ViolationRegistry.getInstance().getPhpcs(fo);
            case PHPMD:
                return ViolationRegistry.getInstance().getPhpmd(fo);
            case PHPCPD:
                return ViolationRegistry.getInstance().getPhpcpd(fo);
            default:
                return new GenericResult(null, null, null);
        }
    }

}
