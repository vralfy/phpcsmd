/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.phpcs;

import de.foopara.phpcsmd.DebugLog;
import de.foopara.phpcsmd.PhpCsMdError;
import de.foopara.phpcsmd.PhpCsMdWarning;
import de.foopara.phpcsmd.generics.GenericOutputReader;
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
        List<PhpCsMdWarning> csWarnings = new ArrayList<PhpCsMdWarning>();
        List<PhpCsMdError> csErrors = new ArrayList<PhpCsMdError>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));
            NodeList ndList = document.getElementsByTagName("warning");
            int lineNum = 0;
            for (int i = 0; i < ndList.getLength(); i++) {
                String message = ndList.item(i).getTextContent();
                NamedNodeMap nm = ndList.item(i).getAttributes();
                lineNum = Integer.parseInt(nm.getNamedItem("line").getTextContent()) - 1;
                csWarnings.add(new PhpCsMdWarning(message, lineNum));
            }
            ndList = document.getElementsByTagName("error");
            for (int i = 0; i < ndList.getLength(); i++) {
                String message = ndList.item(i).getTextContent();
                NamedNodeMap nm = ndList.item(i).getAttributes();
                lineNum = Integer.parseInt(nm.getNamedItem("line").getTextContent()) - 1;
                csErrors.add(new PhpCsMdError(message, lineNum));
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
