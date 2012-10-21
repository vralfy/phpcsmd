package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericFileListener;
import java.util.LinkedHashMap;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class FileListenerRegistry {
    private static FileListenerRegistry instance = null;
    public static FileListenerRegistry getInstance() {
        if (FileListenerRegistry.instance == null) FileListenerRegistry.instance = new FileListenerRegistry();
        return FileListenerRegistry.instance;
    }

    private LinkedHashMap<String, GenericFileListener> _registry = new LinkedHashMap<String, GenericFileListener>();

    public static GenericFileListener getListener(FileObject fo, Lookup lkp) {
        return FileListenerRegistry.getInstance().getNewInstance(fo, lkp);
    }

    public GenericFileListener getNewInstance(FileObject fo, Lookup lkp) {
        if (this._registry.containsKey(fo.getPath())) {
            return this._registry.get(fo.getPath());
        }

        if (fo.isFolder() && !fo.isData()) {
            return null;
        }

        GenericFileListener ret = new GenericFileListener(lkp);

        fo.addFileChangeListener(ret);
        this._registry.put(fo.getPath(), ret);
        return ret;
    }

    public void removeListener(String fo) {
        this._registry.remove(fo);
    }
}