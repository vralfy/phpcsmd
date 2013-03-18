package de.foopara.phpcsmd.option.phpmd;

/**
 *
 * @author nspecht
 */
public class PhpmdSniff
{

    public String name = "";

    public String description = null;

    public String annotationType = null;

    public PhpmdSniff(String name, String description, String annotationType) {
        this.name = name.replaceFirst("PHP_PMD_Rule_", "").replace("_", "");
        this.description = description;
        this.annotationType = annotationType;
    }

    public String getDescription() {
        if (this.description == null) {
            return this.name;
        }
        return this.description;
    }

}
