/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
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
                PdependTypes.PdependMetrics metrics = new PdependTypes.PdependMetrics();
                this.fillValues(ndList.item(i), metrics);
                res.setMetrics(metrics);
            }

            ndList = document.getElementsByTagName("files");
            for (int i = 0; i < ndList.getLength(); i++) {
                NodeList sub = ndList.item(i).getChildNodes();
                for (int j = 0; j < sub.getLength(); j++) {
                    PdependTypes.PdependFile file = new PdependTypes.PdependFile();
                    NamedNodeMap nm = sub.item(j).getAttributes();
                    this.fillValues(sub.item(j), file);
                    res.addFile(file);
                }
            }


        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
        } catch (SAXParseException ex) {
        } catch (SAXException ex) {
        }

        return res;
    }

    private void fillValues(Node node, Object o) {
        if (node == null) return;
        if (node.getAttributes() == null) return;
        for (int i=0; i < node.getAttributes().getLength(); i++) {
            Node attr = node.getAttributes().item(i);
            try {
                Field f = o.getClass().getField(attr.getNodeName());
                String type = f.getType().getCanonicalName();

                if (type.compareTo("java.lang.Integer") == 0 || type.compareTo("int") == 0) {
                    f.setInt(o, Integer.parseInt(attr.getNodeValue()));
                } else if (type.compareTo("java.lang.Float") == 0 || type.compareTo("float") == 0) {
                    f.setFloat(o, Float.parseFloat(attr.getNodeValue()));
                } else if (type.compareTo("java.lang.String") == 0) {
                    f.set(o, attr.getNodeValue() + "");

                }

                else {
                    throw new Exception("Unknown type '" + type + "' for field '" + f.getName() + "'");
                }
            } catch (NoSuchFieldException ex) {
                Exceptions.printStackTrace(ex);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
