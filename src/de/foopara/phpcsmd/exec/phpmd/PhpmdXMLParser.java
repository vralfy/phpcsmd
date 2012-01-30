/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpmd;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

/**
 *
 * @author nspecht
 */
public class PhpmdXMLParser {

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
                violations.add(
                        new GenericViolation(message, start, end)
                        .setAnnotationType("phpmd-violation"));
            }

        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
        } catch (SAXParseException ex) {
        } catch (SAXException ex) {
        }
        /**/

        return new PhpmdResult(null, violations, null);
    }
}
