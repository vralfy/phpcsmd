package de.foopara.phpcsmd.option.phpmd;

import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 *
 * @author nspecht
 */
abstract public class GenericPhpmdSniffRegistry
{

    private static GenericPhpmdSniffRegistry instance = null;

    public static GenericPhpmdSniffRegistry getInstance() {
        if (GenericPhpmdSniffRegistry.instance == null) {
            GenericPhpmdSniffRegistry.instance = new DynamicPhpmdSniffRegistry();
        }
        return GenericPhpmdSniffRegistry.instance;
    }

    LinkedHashMap<String, PhpmdSniff> sniffs = new LinkedHashMap<String, PhpmdSniff>();

    HashSet<String> rulesets = new HashSet<String>();

    public void add(PhpmdSniff sniff) {
        this.sniffs.put(sniff.name, sniff);
    }

    public void add(String ruleset) {
        this.rulesets.add(ruleset);
    }

    public PhpmdSniff get(String name) {
        if (this.sniffs.containsKey(name)) {
            return this.sniffs.get(name);
        }
        for (String key : this.sniffs.keySet()) {
            if (key.endsWith(name)) {
                return this.sniffs.get(key);
            }
        }
        return null;
    }

    public String[] getRulesets() {
        return this.rulesets.toArray(new String[0]);
    }

}
