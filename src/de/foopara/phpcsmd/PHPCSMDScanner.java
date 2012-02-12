/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@ActionID(id = "de.foopara.phpcsmd.PHPCSMDScanner", category = "PHP")
@ActionRegistration(displayName = "#CTL_PHPCSMDScanner",
        iconBase = "de/foopara/phpcsmd/resources/phpcs/error.png")
@ActionReferences({
    @ActionReference(path = "Editors/application/x-directory/Popup", position = 875),
    @ActionReference(path = "Loaders/application/x-directory/Actions", position = 875)
})
@Messages("CTL_PHPCSMDScanner=Scan for violations")
public final class PHPCSMDScanner implements ActionListener {

    private final DataObject context;

    public PHPCSMDScanner(DataObject context) {
        this.context = context;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Scanning directory " + this.context.getPrimaryFile().getPath() + " not supported yes");
    }
}
