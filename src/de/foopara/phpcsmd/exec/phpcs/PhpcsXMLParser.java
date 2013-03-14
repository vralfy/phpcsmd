package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import de.foopara.phpcsmd.option.PhpcsOptions;
import de.foopara.phpcsmd.option.phpcs.GenericPhpcsSniffRegistry;
import de.foopara.phpcsmd.option.phpcs.PhpcsSniff;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Lookup;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author nspecht
 */
public class PhpcsXMLParser {

    private Lookup lkp;

    public PhpcsXMLParser(Lookup lkp) {
        this.lkp = lkp;
    }

    public PhpcsResult parse(GenericOutputReader reader) {
        List<GenericViolation> csWarnings = new ArrayList<GenericViolation>();
        List<GenericViolation> csErrors = new ArrayList<GenericViolation>();
        List<GenericViolation> csExtras = new ArrayList<GenericViolation>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));

            // WARNINGS
            NodeList ndList = document.getElementsByTagName("warning");
            this.parseList(ndList, csWarnings, csExtras, "warning");

            // ERRORS
            ndList = document.getElementsByTagName("error");
            this.parseList(ndList, csErrors, csExtras, "error");
        } catch (SAXException ex) {
            Logger.getInstance().log(ex);
        } catch (ParserConfigurationException ex) {
            Logger.getInstance().log(ex);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } catch (Exception ex) {
            Logger.getInstance().log(ex);
        }

        return new PhpcsResult(csWarnings, csErrors, csExtras);
    }

    public void parseList(NodeList ndList, List<GenericViolation> normalList, List<GenericViolation> extraList, String annotation) {
        for (int i = 0; i < ndList.getLength(); i++) {
            String message = ndList.item(i).getTextContent().trim();
            NamedNodeMap nm = ndList.item(i).getAttributes();
            int lineNum = Integer.parseInt(nm.getNamedItem("line").getTextContent()) - 1;
            String type = nm.getNamedItem("source").getTextContent();

            boolean addNormal = true;
            if ((Boolean)PhpcsOptions.load(PhpcsOptions.Settings.EXTRAS, this.lkp)) {
                for (PhpcsSniff sniff : GenericPhpcsSniffRegistry.getInstance().getFlat()) {
                    if ((type.startsWith(sniff.name) || type.compareTo(sniff.name) == 0)
                        && PhpcsOptions.getSniff(sniff.shortName)
                        && sniff.annotationType != null
                    ) {
                        GenericViolation violation = new GenericViolation(message, lineNum)
                                .setAnnotationType("phpcs-" + sniff.annotationType)
                                .setGroup("phpcs-violation");
                        if (PhpcsOptions.getSniffTask(sniff.shortName)) {
                            normalList.add(violation);
                        } else {
                            extraList.add(violation);
                        }
                        addNormal = false;
                    }
                }
            }

            if (addNormal) {
                normalList.add(new GenericViolation(message, lineNum)
                        .setAnnotationType("phpcs-" + annotation)
                        .setGroup("phpcs-violation")
                );
            }
        }
    }
}
