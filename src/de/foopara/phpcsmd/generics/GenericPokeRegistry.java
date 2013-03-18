package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.ui.reports.ScanReportTopComponent;
import java.util.HashSet;
import org.openide.filesystems.FileObject;

public class GenericPokeRegistry
{

    private static GenericPokeRegistry instance = null;

    public static GenericPokeRegistry getInstance() {
        if (GenericPokeRegistry.instance == null) {
            GenericPokeRegistry.instance = new GenericPokeRegistry();
        }
        return GenericPokeRegistry.instance;
    }

    private HashSet<ScanReportTopComponent> scanReportRegistry = new HashSet<ScanReportTopComponent>();

    public void poke(FileObject fo) {
        this.pokeScanReports(fo);
    }

    private void pokeScanReports(FileObject fo) {
        for (ScanReportTopComponent comp : this.scanReportRegistry) {
            comp.poke(fo);
        }
    }

    public void register(ScanReportTopComponent comp) {
        this.scanReportRegistry.add(comp);
    }

    public void unregister(ScanReportTopComponent comp) {
        this.scanReportRegistry.remove(comp);
    }

}
