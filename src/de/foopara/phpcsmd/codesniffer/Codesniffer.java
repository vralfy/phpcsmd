/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.codesniffer;

/**
 *
 * @author nspecht
 */
public class Codesniffer {
    private static Codesniffer instance = null;

    public static Codesniffer getInstance() {
        if (Codesniffer.instance == null) {
            Codesniffer.instance = new Codesniffer();
        }
        return Codesniffer.instance;
    }
    
    private boolean _enabled = false;
    
    public boolean isEnabled() {
        return this._enabled;
    }
}
