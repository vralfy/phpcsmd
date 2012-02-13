/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.generics.GenericResult;
import de.foopara.phpcsmd.ui.GenericTable;
import org.openide.filesystems.FileObject;

/**
 *
 * @author nspecht
 */
public class ScanReportTable extends GenericTable {

    private FileObject rootDir = null;

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
    }

    public void setRootDirectory(FileObject dir) {
        this.rootDir = dir;
    }

    public void addElement(FileObject fo) {
        if (!fo.getPath().startsWith(this.rootDir.getPath())) return;

        GenericResult phpcs = ViolationRegistry.getInstance().getPhpcs(fo);
        GenericResult phpmd = ViolationRegistry.getInstance().getPhpmd(fo);
        GenericResult phpcpd = ViolationRegistry.getInstance().getPhpcpd(fo);

        if (!(phpcs.getSum() + phpmd.getSum() + phpcpd.getSum() > 0)) return;

        if (phpcs == null)  phpcs  = new GenericResult(null, null, null);
        if (phpmd == null)  phpmd  = new GenericResult(null, null, null);
        if (phpcpd == null) phpcpd = new GenericResult(null, null, null);

        this.model.addRow(new Object[]{
                    fo.getPath().replaceFirst(this.rootDir.getPath(), ""),
                    phpcs.getErrors().size(),
                    phpcs.getWarnings().size(),
                    phpmd.getErrors().size() + phpmd.getWarnings().size(),
                    phpcpd.getErrors().size() + phpcpd.getWarnings().size()
                });
    }
}
