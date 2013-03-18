package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import de.foopara.phpcsmd.option.phpmd.GenericPhpmdSniffRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author nspecht
 */
public class PhpmdXMLParser
{

    public PhpmdResult parse(GenericOutputReader reader) {
        List<GenericViolation> violations = new ArrayList<GenericViolation>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));
            NodeList ndList = document.getElementsByTagName("violation");

            for (int i = 0; i < ndList.getLength(); i++) {
                String message = ndList.item(i).getTextContent().trim();
                NamedNodeMap nm = ndList.item(i).getAttributes();
                int start = Integer.parseInt(nm.getNamedItem("beginline").getTextContent()) - 1;
                int end = Integer.parseInt(nm.getNamedItem("endline").getTextContent()) - 1;
                String sniffClass = nm.getNamedItem("rule").getTextContent();

                String annotationType = "violation";
                if (GenericPhpmdSniffRegistry.getInstance().get(sniffClass) != null
                        && GenericPhpmdSniffRegistry.getInstance().get(sniffClass).annotationType != null) {
                    annotationType = GenericPhpmdSniffRegistry.getInstance().get(sniffClass).annotationType;
                }
                violations.add(
                        new GenericViolation(message, start, end)
                        .setAnnotationType("phpmd-" + annotationType)
                        .setGroup("phpmd-violation")
                        .setMultiline(false));
            }
        } catch (SAXException ex) {
            Logger.getInstance().log(ex);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
        } catch (ParserConfigurationException ex) {
            Logger.getInstance().log(ex);
        } catch (Exception ex) {
            Logger.getInstance().log(ex);
        }

        return new PhpmdResult(null, violations, null);
    }

}
