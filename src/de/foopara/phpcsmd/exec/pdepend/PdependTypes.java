/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import java.util.HashSet;

/**
 *
 * @author n.specht
 */
public class PdependTypes {
    static public class PdependType {
        @Override
        public String toString() {
            return this.getClass().getName();
        }
    }

    static public class PdependMetrics extends PdependTypes.PdependType {
        public float andc;
        public int calls;
        public int ccn;
        public int ccn2;
        public int cloc;

        public int clsa;
        public int clsc;
        public int eloc;
        public int fanout;
        public int leafs;

        public int lloc;
        public int loc;
        public int maxDIT;
        public int ncloc;
        public int noc;

        public int nof;
        public int noi;
        public int nom;
        public int nop;
        public int roots;
    }

    static public class PdependFile extends PdependTypes.PdependType {
        public String name;
        public int cloc;
        public int eloc;
        public int lloc;
        public int loc;
        public int ncloc;
    }

    static public class PdependPackage extends PdependTypes.PdependType {
        public String name;
        public float cr;
        public int noc;
        public int nof;
        public int noi;
        public int nom;
        public float rcr;
        HashSet<PdependClass> classes = new HashSet<PdependClass>();

        public void addClass(PdependClass c) {
            this.classes.add(c);
        }
    }

    static public class PdependClass extends PdependTypes.PdependType {
        public String name;
        public int ca;
        public int cbo;
        public int ce;
        public int cis;

        public int cloc;
        public float cr;
        public int csz;
        public int dit;
        public int eloc;

        public int impl;
        public int lloc;
        public int loc;
        public int ncloc;
        public int noam;

        public int nocc;
        public int nom;
        public int noom;
        public int npm;
        public float rcr;

        public int vars;
        public int varsi;
        public int varsnp;
        public int wmc;
        public int wmci;

        public int wmcnp;

        HashSet<PdependMethod> methods = new HashSet<PdependMethod>();

        public void addClass(PdependMethod m) {
            this.methods.add(m);
        }
    }

    static public class PdependMethod extends PdependTypes.PdependType {
        public String name;
        public int ccn;
        public int ccn2;
        public int cloc;
        public int eloc;
        public int lloc;
        public int loc;
        public int ncloc;
        public int npath;
    }
}
