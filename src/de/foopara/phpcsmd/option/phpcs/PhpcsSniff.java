/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpcs;

/**
 *
 * @author nspecht
 */
public class PhpcsSniff {

    public String name = "";
    public String description = "";
    public String annotationType = "";
    public String shortName = "";

    public PhpcsSniff(String name, String description, String annotationType) {
        this.name = name;
        this.description = description;
        this.shortName = name.replace(".", "").toLowerCase();
        this.annotationType = annotationType;
    }

    public String getDescription() {
        if (this.description != null) {
            return this.description;
        }
        return this.name;
    }
}
