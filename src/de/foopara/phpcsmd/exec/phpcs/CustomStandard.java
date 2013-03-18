package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.option.PhpcsOptions;
import de.foopara.phpcsmd.option.phpcs.GenericPhpcsSniffRegistry;
import de.foopara.phpcsmd.option.phpcs.PhpcsSniff;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author n.specht
 */
public class CustomStandard {
    private File _ruleset;
    private Lookup lkp;

    public CustomStandard() {
        this(null);
    }

    public CustomStandard(Lookup lkp) {
        try {
            this.lkp = lkp;
            this._ruleset = File.createTempFile("phpcsmdCustom",".xml");
            StringBuilder content = new StringBuilder();
            content.append("<?xml version=\"1.0\"?>\n");
            content.append("<ruleset name=\"PhpcsmdCustom\">\n");
            content.append("\t<description>PHPCSMDCUSTOM</description>\n");

            if (((String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, this.lkp)).trim().length() > 0) {
                String standard = (String)PhpcsOptions.load(PhpcsOptions.Settings.STANDARD, this.lkp);
                Logger.getInstance().log("using standard " + standard, "Creating custom phpcs standard");
                content
                        .append("\t<rule ref=\"")
                        .append(standard)
                        .append("\" />\n\n");
            }

            for (PhpcsSniff sniff : GenericPhpcsSniffRegistry.getInstance().getFlat()) {
                if (PhpcsOptions.getSniff(sniff.shortName)) {
                    content.append("\t<rule ref=\"").append(sniff.name).append("\" />\n");
                    if (sniff.getKeyCount() > 1) {
                        for(String sniffKey : sniff.getKeys().keySet()) {
                            if (PhpcsOptions.getSniff(sniffKey)) {
                                content.append("\t<rule ref=\"")
                                        .append(sniff.name)
                                        .append(".")
                                        .append(sniff.getKeys().get(sniffKey))
                                        .append("\">\n")
                                        .append("\t\t<severity>0</severity>\n")
                                        .append("\t</rule>\n");
                            }
                        }
                    }
                }
            }

            content.append("</ruleset>");

            FileWriter f = new FileWriter(this._ruleset);
            f.write(content.toString());
            f.flush();
            f.close();
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String toString() {
        return this._ruleset.getAbsolutePath();
    }

    public void delete() {
        boolean delete = this._ruleset.delete();
        if (!delete) {
            Logger.getInstance().logPre("failed to delete " + this._ruleset.getAbsolutePath(), "phpcs");
        }
    }
}