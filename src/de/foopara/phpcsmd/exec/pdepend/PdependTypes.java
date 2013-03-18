package de.foopara.phpcsmd.exec.pdepend;

import java.util.HashSet;

public class PdependTypes
{

    static public class PdependType
    {

        @Override
        public String toString() {
            return this.getClass().getName();
        }

    }

    static public class PdependMetrics extends PdependTypes.PdependType
    {

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

        @Override
        public String toString() {
            return "Pdepend Metrics";
        }

    }

    static public class PdependFile extends PdependTypes.PdependType
    {

        public String name;     //Filepath

        public int cloc;        //Comment Lines fo Code

        public int eloc;        //Executable Lines of Code

        public int lloc;        //Logical Lines Of Code

        public int loc;         //Lines Of Code

        public int ncloc;       //Non Comment Lines Of Code

        private String _filter = null;

        @Override
        public String toString() {
            if (this._filter != null && this.name != null) {
                if (this.name.startsWith(this._filter)) {
                    return this.name.substring(this._filter.length());
                }
            }
            return this.name;

        }

        public void setFilter(String f) {
            this._filter = f;
        }

    }

    static public class PdependPackage extends PdependTypes.PdependType
    {
        //summary

        public String name;     //Packagename

        public float cr;        //Code Rank

        public int noc;         //Number Of Classes

        public int nof;         //Number Of Functions

        public int noi;         //Number Of Interfaces

        public int nom;         //Number Of Methods

        public float rcr;       //Reverse Code Rank

        private HashSet<PdependClass> classes = new HashSet<PdependClass>();

        //jdepend
        public int TotalClasses;

        public int ConcreteClasses;

        public int AbstractClasses;

        public int Ca;  //Afferent Couplings

        public int Ce;  //Efferent Couplings

        public float A; //Abstractnes

        public float I; //Instability

        public float D; //Distance

        private HashSet<PdependPackage> dependsUpon = new HashSet<PdependPackage>();

        private HashSet<PdependPackage> usedBy = new HashSet<PdependPackage>();

        public void addClass(PdependClass c) {
            this.classes.add(c);
        }

        public HashSet<PdependClass> getClasses() {
            return this.classes;
        }

        public void dependsUpon(PdependPackage p) {
            this.dependsUpon.add(p);
        }

        public HashSet<PdependPackage> getDepends() {
            return this.dependsUpon;
        }

        public void usedBy(PdependPackage p) {
            this.usedBy.add(p);
        }

        public HashSet<PdependPackage> getUsedBy() {
            return this.usedBy;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    static public class PdependClass extends PdependTypes.PdependType
    {

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

        private HashSet<PdependMethod> methods = new HashSet<PdependMethod>();

        private String _fileName = null;

        public void addMethod(PdependMethod m) {
            this.methods.add(m);
            m.setFilename(this._fileName);
        }

        public HashSet<PdependMethod> getMethods() {
            return this.methods;
        }

        public void setFilename(String name) {
            this._fileName = name;
            for (PdependMethod m : this.methods) {
                m.setFilename(this._fileName);
            }
        }

        public String getFilename() {
            return this._fileName;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    static public class PdependMethod extends PdependTypes.PdependType
    {

        public String name;     //Methodname

        public int ccn;         //Cyclomatic Complexity Number

        public int ccn2;        //Extended Cyclomatic Complexity Number

        public int cloc;        //Comment Lines fo Code

        public int eloc;        //Executable Lines of Code

        public int lloc;        //Logical Lines Of Code

        public int loc;         //Lines Of Code

        public int ncloc;       //Non Comment Lines Of Code

        public float npath;     //NPath Complexity

        private String _fileName = null;

        public void setFilename(String name) {
            this._fileName = name;
        }

        public String getFilename() {
            return this._fileName;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    static public class PdependFunction extends PdependTypes.PdependType
    {

        public String name;     //Functionname

        public int ccn;         //Cyclomatic Complexity Number

        public int ccn2;        //Extended Cyclomatic Complexity Number

        public int cloc;        //Comment Lines fo Code

        public int eloc;        //Executable Lines of Code

        public int lloc;        //Logical Lines Of Code

        public int loc;         //Lines Of Code

        public int ncloc;       //Non Comment Lines Of Code

        public float npath;     //NPath Complexity

        private String _fileName = null;

        public void setFilename(String name) {
            this._fileName = name;
        }

        public String getFilename() {
            return this._fileName;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }
}
