/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericFileListener;
import java.util.LinkedHashMap;
import org.openide.filesystems.FileObject;

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

    public static GenericFileListener getListener(FileObject fo) {
        return FileListenerRegistry.getInstance().getNewInstance(fo);
    }

    public GenericFileListener getNewInstance(FileObject fo) {
        if (this._registry.containsKey(fo.getPath())) {
            return this._registry.get(fo.getPath());
        }
        GenericFileListener ret = new GenericFileListener();
        
        fo.addFileChangeListener(ret);
        this._registry.put(fo.getPath(), ret);
        return ret;
    }
}