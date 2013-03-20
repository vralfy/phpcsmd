package de.foopara.phpcsmd.exec.pdepend;

import java.io.Serializable;
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

    static public class PdependMetrics extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;

        public String generated = ""; //Creation time

        public String pdepend = "";   //Pdepend version

        public float ahh = 0f;        //Average Hierarchy Height

        public float andc = 0f;       //Average Number of Derived Classes

        public int calls = 0;         //Number of Method or Function Calls

        public int ccn = 0;           //Cyclomatic Complexity Number

        public int ccn2 = 0;          //Extended Cyclomatic Complexity Number

        public int cloc = 0;          //Comment Lines fo Code

        public int clsa = 0;          //Number of Abstract Classes

        public int clsc = 0;          //Number of Concrete Classes

        public int eloc = 0;          //Executable Lines of Code

        public int fanout = 0;        //Number of Fanouts

        public int leafs = 0;         //Number of Leaf (final) Classes

        public int lloc = 0;          //Logical Lines Of Code

        public int loc = 0;           //Lines Of Code

        public int maxDIT = 0;        //Max Depth of Inheritance Tree

        public int ncloc = 0;         //Non Comment Lines Of Code

        public int noc = 0;           //Number Of Classes

        public int nof = 0;           //Number Of Functions

        public int noi = 0;           //Number Of Interfaces

        public int nom = 0;           //Number Of Methods

        public int nop = 0;           //Number of Packages

        public int roots = 0;         //Number of Root Classes

        @Override
        public String toString() {
            return "Pdepend Metrics";
        }

    }

    static public class PdependFile extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;
        
        public String name = "";     //Filepath

        public int cloc = 0;        //Comment Lines fo Code

        public int eloc = 0;        //Executable Lines of Code

        public int lloc = 0;        //Logical Lines Of Code

        public int loc = 0;         //Lines Of Code

        public int ncloc = 0;       //Non Comment Lines Of Code

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

    static public class PdependPackage extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;

        //summary

        public String name = "";     //Packagename

        public float cr = 0f;        //Code Rank

        public int noc = 0;          //Number Of Classes

        public int nof = 0;          //Number Of Functions

        public int noi = 0;          //Number Of Interfaces

        public int nom = 0;          //Number Of Methods

        public float rcr = 0f;       //Reverse Code Rank

        private final HashSet<PdependClass> classes = new HashSet<PdependClass>();

        //jdepend
        public int TotalClasses = 0;

        public int ConcreteClasses = 0;

        public int AbstractClasses = 0;

        public int Ca = 0;           //Afferent Couplings

        public int Ce = 0;           //Efferent Couplings

        public float A = 0f;         //Abstractnes

        public float I = 0f;         //Instability

        public float D = 0f;         //Distance

        private final HashSet<PdependPackage> dependsUpon = new HashSet<PdependPackage>();

        private final HashSet<PdependPackage> usedBy = new HashSet<PdependPackage>();

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

    static public class PdependClass extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;

        public String name = "";     //Classname

        public int ca = 0;           //Afferent Coupling

        public int cbo = 0;          //Coupling Between Objects

        public int ce = 0;           //Efferent Coupling

        public int cis = 0;          //Class Interface Size

        public int cloc = 0;         //Comment Lines fo Code

        public float cr = 0f;        //Code Rank

        public int csz = 0;          //Class Size

        public int dit = 0;          //Depth of Inheritance Tree

        public int eloc = 0;         //Executable Lines of Code

        public int impl = 0;         //-------------------------------------------------------

        public int lloc = 0;         //Logical Lines Of Code

        public int loc = 0;          //Lines Of Code

        public int ncloc = 0;        //Non Comment Lines Of Code

        public int noam = 0;         //Number Of Added Methods

        public int nocc = 0;         //Number Of Child Classes

        public int nom = 0;          //Number Of Methods

        public int noom = 0;         //Number Of Overwritten Methods

        public int npm = 0;          //Number of Public Methods

        public float rcr = 0f;       //Reverse Code Rank

        public int vars = 0;         //Properties

        public int varsi = 0;        //Inherited Properties

        public int varsnp = 0;       //Non Private Properties

        public int wmc = 0;          //Weighted Method Count

        public int wmci = 0;         //Inherited Weighted Method Count

        public int wmcnp = 0;        //Non Private Weighted Method Count

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

    static public class PdependMethod extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;

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

    static public class PdependFunction extends PdependTypes.PdependType implements Serializable
    {
        public static final long serialVersionUID = 1L;

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
