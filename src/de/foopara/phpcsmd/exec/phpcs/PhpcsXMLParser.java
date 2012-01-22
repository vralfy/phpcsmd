/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcs;

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
public class PhpcsXMLParser {

    public PhpcsResult parse(GenericOutputReader reader) {
        List<GenericViolation> csWarnings = new ArrayList<GenericViolation>();
        List<GenericViolation> csErrors = new ArrayList<GenericViolation>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));
            NodeList ndList = document.getElementsByTagName("warning");

            for (int i = 0; i < ndList.getLength(); i++) {
                String message = ndList.item(i).getTextContent().trim();
                NamedNodeMap nm = ndList.item(i).getAttributes();
                int lineNum = Integer.parseInt(nm.getNamedItem("line").getTextContent()) - 1;
                csWarnings.add(
                        new GenericViolation(message, lineNum)
                        .setAnnotationType("phpcs-warning")
                );
            }
            ndList = document.getElementsByTagName("error");
            for (int i = 0; i < ndList.getLength(); i++) {
                String message = ndList.item(i).getTextContent().trim();
                NamedNodeMap nm = ndList.item(i).getAttributes();
                int lineNum = Integer.parseInt(nm.getNamedItem("line").getTextContent()) - 1;
                csErrors.add(
                        new GenericViolation(message, lineNum)
                        .setAnnotationType("phpcs-error")
                );
            }
        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
        } catch (SAXParseException ex) {
        } catch (SAXException ex) {
        }
        /**/
        
        return new PhpcsResult(csWarnings, csErrors);
    }
}
