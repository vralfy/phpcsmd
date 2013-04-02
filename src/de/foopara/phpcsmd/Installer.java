/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import de.foopara.phpcsmd.generics.GenericExecute;
import de.foopara.phpcsmd.generics.GenericHelper;
import de.foopara.phpcsmd.option.GeneralOptions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @see http://wiki.netbeans.org/DevFaqListenForOpenEvents
 * @see http://wiki.netbeans.org/DevFaqModulesDeclarativeVsProgrammatic
 * @author nspecht
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        WindowManager.getDefault().invokeWhenUIReady(new OpenListenerInstaller());
    }

    private class OpenListenerInstaller implements Runnable {
        @Override
        public void run() {
            WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("opened")) {
                        HashSet<TopComponent> newHashSet = (HashSet<TopComponent>) evt.getNewValue();
                        HashSet<TopComponent> oldHashSet = (HashSet<TopComponent>) evt.getOldValue();
                        for (Iterator<TopComponent> it = newHashSet.iterator(); it.hasNext();) {
                            TopComponent topComponent = it.next();
                            if (!oldHashSet.contains(topComponent)) {
                                DataObject dObj = topComponent.getLookup().lookup(DataObject.class);
                                if (dObj != null) {
                                    FileObject file = dObj.getPrimaryFile();
                                    if (file != null && GenericHelper.isDesirableFile(file)) {

                                        Lookup lookup = GenericHelper.getFileLookup(file);
                                        if ((Boolean)GeneralOptions.load(GeneralOptions.Settings.CHECKONOPEN, lookup)) {
                                            GenericExecute.executeQATools(file);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

}