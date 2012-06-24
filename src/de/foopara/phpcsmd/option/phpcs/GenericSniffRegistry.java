/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.option.phpcs;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 *
 * @author nspecht
 */
abstract public class GenericSniffRegistry {

    private static GenericSniffRegistry instance = null;
    public static GenericSniffRegistry getInstance() {
        if (GenericSniffRegistry.instance == null) {
            GenericSniffRegistry.instance = new DynamicSniffRegistry();
        }
        return GenericSniffRegistry.instance;
    }

    public class SniffClass {
        LinkedHashMap<String, PhpcsSniff> sniffs = new LinkedHashMap<String, PhpcsSniff>();

        public void addSniff(String name, PhpcsSniff sniff) {
            this.sniffs.put(name, sniff);
        }

        public HashSet<PhpcsSniff> getFlat() {
            HashSet<PhpcsSniff> ret = new HashSet<PhpcsSniff>();
            for (PhpcsSniff sniff : this.sniffs.values()) {
                ret.add(sniff);
            }
            return ret;
        }

        public Collection<PhpcsSniff> getSniffs() {
            return this.sniffs.values();
        }

        public int getSize() {
            return this.sniffs.size();
        }
    }

    public class SniffStandard {
        LinkedHashMap<String, GenericSniffRegistry.SniffClass> classes = new LinkedHashMap<String, GenericSniffRegistry.SniffClass>();

        public SniffClass getSniffClass(String name) {
            if (!this.classes.containsKey(name)) {
                this.classes.put(name, new SniffClass());
            }
            return this.classes.get(name);
        }

        public HashSet<PhpcsSniff> getFlat() {
            HashSet<PhpcsSniff> ret = new HashSet<PhpcsSniff>();
            for (SniffClass sniffClass : this.classes.values()) {
                ret.addAll(sniffClass.getFlat());
            }
            return ret;
        }

        public Collection<String> getClasses() {
            return this.classes.keySet();
        }

        public GenericSniffRegistry.SniffClass getClass(String name) {
            return this.classes.get(name);
        }
    }


    LinkedHashMap<String, GenericSniffRegistry.SniffStandard> standards = new LinkedHashMap<String, GenericSniffRegistry.SniffStandard>();

    public void add(PhpcsSniff sniff) {
        String[] path = sniff.name.split("\\.");
        if (!this.standards.containsKey(path[0])) {
            this.standards.put(path[0], new SniffStandard());
        }

        this.standards.get(path[0]).getSniffClass(path[1]).addSniff(path[2], sniff);
    }

    public HashSet<PhpcsSniff> getFlat() {
        HashSet<PhpcsSniff> ret = new HashSet<PhpcsSniff>();
        for (SniffStandard st : this.standards.values()) {
            ret.addAll(st.getFlat());
        }
        return ret;
    }

    public Collection<String> getStandards() {
        return this.standards.keySet();
    }

    public GenericSniffRegistry.SniffStandard getStandard(String name) {
        return this.standards.get(name);
    }
}
