package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.option.PhpcsOptions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author n.specht
 */
public class CustomStandard {
    private File _ruleset;
    public CustomStandard() {
        try {
            this._ruleset = File.createTempFile("phpcsmdCustom",".xml");
            StringBuilder content = new StringBuilder();
            content.append("<?xml version=\"1.0\"?>\n");
            content.append("<ruleset name=\"PhpcsmdCustom\">\n");
            content.append("\t<description>PHPCSMDCUSTOM</description>\n");
            content.append("\t<rule ref=\"").append(PhpcsOptions.getStandard()).append("\" />\n\n");

            if (PhpcsOptions.getXUnreachable()) {
                content.append("\t<rule ref=\"Squiz.PHP.NonExecutableCode.Unreachable\" />\n");
            }
            content.append("</ruleset>");

            FileWriter f = new FileWriter(this._ruleset);
            f.write(content.toString());
            f.flush();
            f.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String toString() {
        return this._ruleset.getAbsolutePath();
    }

    public void delete() {
        this._ruleset.delete();
    }
}
