package de.foopara.phpcsmd.ui.reports;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.ui.GenericTable;

/**
 *
 * @author nspecht
 */
public class ScanReportTable extends GenericTable
{

    private FileObject rootDir = null;

    private Integer phpcs_errors = 0;

    private Integer phpcs_warnings = 0;

    private Integer phpmd_errors = 0;

    private Integer phpmd_warnings = 0;

    private Integer phpcpd_errors = 0;

    private Integer phpcpd_warnings = 0;

    public ScanReportTable() {
        super();
        this.model.addColumn("File");
        this.model.addColumn("phpcs-Errors");
        this.model.addColumn("phpcs-Warnings");
        this.model.addColumn("phpmd");
        this.model.addColumn("phpcpd");

        for (int i = 1; i <= 2; i++) {
            this.getColumnModel().getColumn(i).setMaxWidth(120);
            this.getColumnModel().getColumn(i).setMinWidth(120);
        }
        for (int i = 3; i <= 4; i++) {
            this.getColumnModel().getColumn(i).setMaxWidth(80);
            this.getColumnModel().getColumn(i).setMinWidth(80);
        }

        this.finishTableSettings();

        this.sorter.setComparator(0, new GenericTable.FilepathComparator());
        for (int i = 1; i <= 4; i++) {
            this.sorter.setComparator(i, new GenericTable.IntegerComparator());
        }

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

    @Override
    public void flushElements() {
        super.flushElements();
        this.phpcs_errors = 0;
        this.phpcs_warnings = 0;
        this.phpmd_errors = 0;
        this.phpmd_warnings = 0;
        this.phpcpd_errors = 0;
        this.phpcpd_warnings = 0;
    }

    public void setRootDirectory(FileObject dir) {
        this.rootDir = dir;
    }

    public void addElement(FileObject fo) {

        if (!fo.getPath().startsWith(this.rootDir.getPath())) {
            return;
        }

        String printedName = fo.getPath().replaceFirst(this.rootDir.getPath(), "");
        boolean doubleEntry = false;
        for (int i = 0; i < this.model.getRowCount(); i++) {
            String val = (String)this.model.getValueAt(i, 0);
            if (val.compareTo(printedName) == 0) {
                doubleEntry = true;
                this.model.removeRow(i);
            }
        }

        GenericResult phpcs = ViolationRegistry.getInstance().getPhpcs(fo);
        GenericResult phpmd = ViolationRegistry.getInstance().getPhpmd(fo);
        GenericResult phpcpd = ViolationRegistry.getInstance().getPhpcpd(fo);

        if (phpcs == null) {
            phpcs = new GenericResult(null, null, null);
        }
        if (phpmd == null) {
            phpmd = new GenericResult(null, null, null);
        }
        if (phpcpd == null) {
            phpcpd = new GenericResult(null, null, null);
        }

        if (!(phpcs.getSum() + phpmd.getSum() + phpcpd.getSum() > 0)) {
            return;
        }

        this.model.addRow(new Object[]{
            printedName,
            phpcs.getErrors().size(),
            phpcs.getWarnings().size(),
            phpmd.getErrors().size() + phpmd.getWarnings().size(),
            phpcpd.getErrors().size() + phpcpd.getWarnings().size(),
        });

        if (!doubleEntry) {
            this.phpcs_errors += phpcs.getErrors().size();
            this.phpcs_warnings += phpcs.getWarnings().size();
            this.phpmd_errors += phpmd.getErrors().size();
            this.phpmd_warnings += phpmd.getWarnings().size();
            this.phpcpd_errors += phpcpd.getErrors().size();
            this.phpcpd_warnings += phpcpd.getWarnings().size();
        }
    }

    public void poke(FileObject fo) {
        if (!fo.getPath().startsWith(this.rootDir.getPath())) {
            return;
        }
        String subpath = fo.getPath().replaceFirst(this.rootDir.getPath(), "");
        boolean foundEntry = false;
        for (int i = this.model.getRowCount() - 1; i >= 0 ; i--) {
            if (subpath.compareTo((String)this.model.getValueAt(i, 0)) == 0) {
                foundEntry = true;
                this.phpcs_errors -= (Integer)this.model.getValueAt(i, 1);
                this.phpcs_warnings -= (Integer)this.model.getValueAt(i, 2);
                this.phpmd_errors -= (Integer)this.model.getValueAt(i, 3);
//                this.phpmd_warnings -= 0;
                this.phpcpd_errors -= (Integer)this.model.getValueAt(i, 4);
//                this.phpcpd_warnings -= 0;
                if (ViolationRegistry.getInstance().getViolationsCount(fo) > 0) {
                    //CASE: Immer noch violations
                    this.phpcs_errors += ViolationRegistry.getInstance().getPhpcs(fo).getErrors().size();
                    this.phpcs_warnings += ViolationRegistry.getInstance().getPhpcs(fo).getWarnings().size();
                    this.phpmd_errors += ViolationRegistry.getInstance().getPhpmd(fo).getErrors().size();
                    this.phpmd_warnings += ViolationRegistry.getInstance().getPhpmd(fo).getWarnings().size();
                    this.phpcpd_errors += ViolationRegistry.getInstance().getPhpcpd(fo).getErrors().size();
                    this.phpcpd_warnings += ViolationRegistry.getInstance().getPhpcpd(fo).getWarnings().size();

                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpcs(fo).getErrors().size(), i, 1);
                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpcs(fo).getWarnings().size(), i, 2);
                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpmd(fo).getErrors().size(), i, 3);
//                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpmd(fo).getWarnings().size(), i, 2);
                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpcpd(fo).getErrors().size(), i, 4);
//                    this.model.setValueAt(ViolationRegistry.getInstance().getPhpcpd(fo).getWarnings().size(), i, 2);
                } else {
                    //CASE: Super, keine Violations mehr
                    this.model.removeRow(i);
                }
            }
        }
        if (!foundEntry) {
            //CASE: Verdammt! Eine Datei hat neue Violations erhalten, obwohl sie vorher sauber war
            this.addElement(fo);
        }
        //Sortiere neu ... ausgestellt da bei vielen Dateien das ganze einfach nur nervig ist ...
//        this.model.fireTableDataChanged();
    }

    public Integer getFilesCount() {
        return this.model.getRowCount();
    }

    public Integer getPhpcsErrorsCount() {
        return this.phpcs_errors;
    }

    public Integer getPhpcsWarningsCount() {
        return this.phpcs_warnings;
    }

    public Integer getPhpmdErrorsCount() {
        return this.phpmd_errors;
    }

    public Integer getPhpmdWarningsCount() {
        return this.phpmd_warnings;
    }

    public Integer getPhpcpdErrorsCount() {
        return this.phpcpd_errors;
    }

    public Integer getPhpcpdWarningsCount() {
        return this.phpcpd_warnings;
    }

    public Integer getViolationCount() {
        return this.getPhpcsErrorsCount() + this.getPhpcsWarningsCount()
                + this.getPhpmdErrorsCount() + this.getPhpmdWarningsCount()
                + this.getPhpcpdErrorsCount() + this.getPhpcpdWarningsCount();
    }

    public void openSelectedFile() {
        String path = (String)this.getValueAt(this.getSelectedRow(), 0);
        FileObject fo = FileUtil.toFileObject(new File(this.rootDir.getPath(), path));
        if (!GenericHelper.isDesirableFile(fo)) {
            return;
        }

        Openable oc = GenericHelper.getFileLookup(fo).lookup(Openable.class);
        if (oc != null) {
            oc.open();
        }

    }
}
