package de.foopara.phpcsmd.option.phpcs;

import java.util.LinkedHashMap;

public class PhpcsKeys {
    protected LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();

    public void addKey(String name) {
        String id = name.replaceAll("\\.", "__").toLowerCase();
        this.keys.put(id, name);
    }

    public int getCount() {
        return this.keys.size();
    }

    public LinkedHashMap<String, String> getKeys() {
        return keys;
    }
}
