/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author nspecht
 */
public class PdependParser {

    public PdependResult parse(GenericOutputReader reader) {
        PdependResult res = new PdependResult();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));

            NodeList ndList = document.getElementsByTagName("metrics");
            for (int i = 0; i < ndList.getLength(); i++) {
                PdependTypes.PdependMetrics metrics = PdependTypes.PdependMetrics.class.newInstance();

                NamedNodeMap nm = ndList.item(i).getAttributes();
                metrics.andc = Integer.parseInt(nm.getNamedItem("andc").getTextContent());
                metrics.calls = Integer.parseInt(nm.getNamedItem("calls").getTextContent());
                metrics.ccn = Integer.parseInt(nm.getNamedItem("ccn").getTextContent());
                metrics.ccn2 = Integer.parseInt(nm.getNamedItem("ccn2").getTextContent());
                metrics.cloc = Integer.parseInt(nm.getNamedItem("cloc").getTextContent());

                res.setMetrics(metrics);
            }
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
        } catch (SAXParseException ex) {
        } catch (SAXException ex) {
        }

        return res;
    }
}
