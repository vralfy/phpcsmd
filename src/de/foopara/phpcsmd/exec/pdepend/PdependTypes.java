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
        public String generated;//Creation time
        public String pdepend;  //Pdepend version
        public float ahh;       //Average Hierarchy Height
        public float andc;      //Average Number of Derived Classes
        public int calls;       //Number of Method or Function Calls
        public int ccn;         //Cyclomatic Complexity Number
        public int ccn2;        //Extended Cyclomatic Complexity Number
        public int cloc;        //Comment Lines fo Code

        public int clsa;        //Number of Abstract Classes
        public int clsc;        //Number of Concrete Classes
        public int eloc;        //Executable Lines of Code
        public int fanout;      //Number of Fanouts
        public int leafs;       //Number of Leaf (final) Classes

        public int lloc;        //Logical Lines Of Code
        public int loc;         //Lines Of Code
        public int maxDIT;      //Max Depth of Inheritance Tree
        public int ncloc;       //Non Comment Lines Of Code
        public int noc;         //Number Of Classes

        public int nof;         //Number Of Functions
        public int noi;         //Number Of Interfaces
        public int nom;         //Number Of Methods
        public int nop;         //Number of Packages
        public int roots;       //Number of Root Classes
    }

    static public class PdependFile extends PdependTypes.PdependType {
        public String name;     //Filepath
        public int cloc;        //Comment Lines fo Code
        public int eloc;        //Executable Lines of Code
        public int lloc;        //Logical Lines Of Code
        public int loc;         //Lines Of Code
        public int ncloc;       //Non Comment Lines Of Code
    }

    static public class PdependPackage extends PdependTypes.PdependType {
        public String name;     //Packagename
        public float cr;        //Code Rank
        public int noc;         //Number Of Classes
        public int nof;         //Number Of Functions
        public int noi;         //Number Of Interfaces
        public int nom;         //Number Of Methods
        public float rcr;       //Reverse Code Rank
        HashSet<PdependClass> classes = new HashSet<PdependClass>();

        public void addClass(PdependClass c) {
            this.classes.add(c);
        }
    }

    static public class PdependClass extends PdependTypes.PdependType {
        public String name;     //Classname
        public int ca;          //Afferent Coupling
        public int cbo;         //Coupling Between Objects
        public int ce;          //Efferent Coupling
        public int cis;         //Class Interface Size

        public int cloc;        //Comment Lines fo Code
        public float cr;        //Code Rank
        public int csz;         //Class Size
        public int dit;         //Depth of Inheritance Tree
        public int eloc;        //Executable Lines of Code

        public int impl;        //-------------------------------------------------------
        public int lloc;        //Logical Lines Of Code
        public int loc;         //Lines Of Code
        public int ncloc;       //Non Comment Lines Of Code
        public int noam;        //Number Of Added Methods

        public int nocc;        //Number Of Child Classes
        public int nom;         //Number Of Methods
        public int noom;        //Number Of Overwritten Methods
        public int npm;         //Number of Public Methods
        public float rcr;       //Reverse Code Rank

        public int vars;        //Properties
        public int varsi;       //Inherited Properties
        public int varsnp;      //Non Private Properties
        public int wmc;         //Weighted Method Count
        public int wmci;        //Inherited Weighted Method Count

        public int wmcnp;       //Non Private Weighted Method Count

        HashSet<PdependMethod> methods = new HashSet<PdependMethod>();

        public void addClass(PdependMethod m) {
            this.methods.add(m);
        }
    }

    static public class PdependMethod extends PdependTypes.PdependType {
        public String name;     //Methodname
        public int ccn;         //Cyclomatic Complexity Number
        public int ccn2;        //Extended Cyclomatic Complexity Number
        public int cloc;        //Comment Lines fo Code
        public int eloc;        //Executable Lines of Code
        public int lloc;        //Logical Lines Of Code
        public int loc;         //Lines Of Code
        public int ncloc;       //Non Comment Lines Of Code
        public int npath;       //NPath Complexity
    }
}
