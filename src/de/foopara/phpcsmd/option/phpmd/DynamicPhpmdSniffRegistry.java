package de.foopara.phpcsmd.option.phpmd;

public class DynamicPhpmdSniffRegistry extends GenericPhpmdSniffRegistry {
    public DynamicPhpmdSniffRegistry() {
        this.add("complexity");
        this.add(new PhpmdSniff("PHP_PMD_Rule_CyclomaticComplexity", "Complexity is determined by the number of decision points in a method plus one for the method entry.  The decision points are 'if', 'while', 'for', and 'case labels'.  Generally, 1-4 is low complexity, 5-7 indicates moderate complexity, 8-10 is high complexity, and 11+ is very high complexity.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_NpathComplexity", "The NPath complexity of a method is the number of acyclic execution paths through that method. A threshold of 200 is generally considered the point where measures should be taken to reduce complexity.", "complex"));
        this.add("codesize");
        this.add(new PhpmdSniff("PHP_PMD_Rule_CyclomaticComplexity", "Complexity is determined by the number of decision points in a method plus one for the method entry.  The decision points are 'if', 'while', 'for', and 'case labels'.  Generally, 1-4 is low complexity, 5-7 indicates moderate complexity, 8-10 is high complexity, and 11+ is very high complexity.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_NpathComplexity", "The NPath complexity of a method is the number of acyclic execution paths through that method. A threshold of 200 is generally considered the point where measures should be taken to reduce complexity.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_LongMethod", "Violations of this rule usually indicate that the method is doing too much.  Try to reduce the method size by creating helper methods and removing any copy/pasted code.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_LongClass", "Long Class files are indications that the class may be trying to do too much.  Try to break it down, and reduce the size to something manageable.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_LongParameterList", "Long parameter lists can indicate that a new object should be created to wrap the numerous parameters.  Basically, try to group the parameters together.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_ExcessivePublicCount", "A large number of public methods and attributes declared in a class can indicate the class may need to be broken up as increased effort will be required to thoroughly test it.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_TooManyFields", "Classes that have too many fields could be redesigned to have fewer fields, possibly through some nested object grouping of some of the information. For example, a class with city/state/zip fields could instead have one Address field.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_TooManyMethods", "A class with too many methods is probably a good suspect for refactoring, in order to reduce its complexity and find a way to have more fine grained objects.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_WeightedMethodCount", "The WMC of a class is a good indicator of how much time and effort is required to modify and maintain this class. A large number of methods also means that this class has a greater potential impact on derived classes.", "complex"));
        this.add("naming");
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_ShortVariable", "Detects when a field, local, or parameter has a very short name.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_LongVariable", "Detects when a field, formal or local variable is declared with a long name.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_ShortMethodName", "Detects when very short method names are used.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_ConstructorWithNameAsEnclosingClass", "A constructor method should not have the same name as the enclosing class, consider to use the PHP 5 __construct method.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_ConstantNamingConventions", "Class/Interface constant nanmes should always be defined in uppercase.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Naming_BooleanGetMethodName", "Looks for methods named 'getX()' with 'boolean' as the return type. The convention is to name these methods 'isX()' or 'hasX()'.", null));
        this.add("controversial");
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_Superglobals", "Accessing a super-global variable directly is considered a bad practice. These variables should be encapsulated in objects that are provided by a framework, for instance.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_CamelCaseClassName", "It is considered best practice to use the CamelCase notation to name classes.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_CamelCasePropertyName", "It is considered best practice to use the camelCase notation to name attributes.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_CamelCaseMethodName", "It is considered best practice to use the CamelCase notation to name methods.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_CamelCaseParameterName", "It is considered best practice to use the camelCase notation to name parameters.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Controversial_CamelCaseVariableName", "It is considered best practice to use the camelCase notation to name variables.", null));
        this.add("unusedcode");
        this.add(new PhpmdSniff("PHP_PMD_Rule_UnusedPrivateField", "Detects when a private field is declared and/or assigned a value, but not used.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_UnusedLocalVariable", "Detects when a local variable is declared and/or assigned, but not used.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_UnusedPrivateMethod", "Unused Private Method detects when a private method is declared but is unused.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_UnusedFormalParameter", "Avoid passing parameters to methods or constructors and then not using those parameters.", null));
        this.add("design");
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_ExitExpression", "An exit-expression within regular code is untestable and therefore it should be avoided. Consider to move the exit-expression into some kind of startup script where an error/exception code is returned to the calling environment.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_EvalExpression", "An eval-expression is untestable, a security risk and bad practice. Therefore it should be avoided. Consider to replace the eval-expression with regular code.", null));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_GotoStatement", "Goto makes code harder to read and it is nearly impossible to understand the control flow of an application that uses this language construct. Therefore it should be avoided. Consider to replace Goto with regular control structures and separate methods/function, which are easier to read.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_NumberOfChildren", "A class with an excessive number of children is an indicator for an unbalanced class hierarchy. You should consider to refactor this class hierarchy.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_DepthOfInheritance", "A class with many parents is an indicator for an unbalanced and wrong class hierarchy. You should consider to refactor this class hierarchy.", "complex"));
        this.add(new PhpmdSniff("PHP_PMD_Rule_Design_CouplingBetweenObjects", "A class with to many dependencies has negative impacts on several quality aspects of a class. This includes quality criterias like stability, maintainability and understandability", "complex"));
}}