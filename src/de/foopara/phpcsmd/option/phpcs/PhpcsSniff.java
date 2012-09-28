/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpcs;

import java.util.LinkedHashMap;

/**
 *
 * @author nspecht
 */
public class PhpcsSniff {

    public String name = "";
    public String description = null;
    public String annotationType = null;
    public String shortName = "";
    public PhpcsKeys keys = null;

    public PhpcsSniff(String name, String description, String annotationType, PhpcsKeys keys) {
        this.name = name;
        this.description = description;
        this.shortName = name.replace(".", "").toLowerCase();
        this.annotationType = annotationType;
        this.keys  = keys;
    }

    public String getDescription() {
        if (this.description != null) {
            return this.description;
        }
        return this.name;
    }

    public int getKeyCount() {
        if (this.keys == null) {
            return 0;
        }
        return this.keys.getCount();
    }

    public LinkedHashMap<String, String> getKeys() {
        if (this.keys == null) {
            return null;
        }
        return this.keys.getKeys();
    }
}
