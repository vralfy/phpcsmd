/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.exec.pdepend.PdependTypes.PdependMethod;
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
            this.fillMetrics(ndList, res);

            ndList = document.getElementsByTagName("files");
            this.fillFiles(ndList, res);

            ndList = document.getElementsByTagName("package");
            this.fillPackages(ndList, res);

        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
        } catch (SAXParseException ex) {
        } catch (SAXException ex) {
        }

        return res;
    }

    private void fillValues(Node node, Object o) {
        if (node == null) {
            return;
        }
        if (node.getAttributes() == null) {
            return;
        }
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
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

                } else {
                    throw new Exception("Unknown type '" + type + "' for field '" + f.getName() + "'");
                }
            } catch (NoSuchFieldException ex) {
                Exceptions.printStackTrace(ex);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void fillMetrics(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            PdependTypes.PdependMetrics _metrics = new PdependTypes.PdependMetrics();
            this.fillValues(ndList.item(i), _metrics);
            res.setMetrics(_metrics);
        }
    }

    private void fillFiles(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            NodeList sub = ndList.item(i).getChildNodes();
            for (int j = 0; j < sub.getLength(); j++) {
                PdependTypes.PdependFile _file = new PdependTypes.PdependFile();
                this.fillValues(sub.item(j), _file);
                res.addFile(_file);
            }
        }
    }

    private void fillPackages(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            PdependTypes.PdependPackage _package = new PdependTypes.PdependPackage();
            this.fillValues(ndList.item(i), _package);
            res.addPackage(_package);
            this.fillClasses(ndList.item(i).getChildNodes(), _package);
        }
    }

    private void fillClasses(NodeList ndList, PdependTypes.PdependPackage res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            if (ndList.item(i).getNodeName().compareTo("class") == 0) {
                PdependTypes.PdependClass _class = new PdependTypes.PdependClass();
                this.fillValues(ndList.item(i), _class);
                res.addClass(_class);
                this.fillMethods(ndList.item(i).getChildNodes(), _class);
            }
        }
    }

    private void fillMethods(NodeList ndList, PdependTypes.PdependClass res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            if (ndList.item(i).getNodeName().compareTo("method") == 0) {
                PdependMethod _method = new PdependTypes.PdependMethod();
                this.fillValues(ndList.item(i), _method);
                res.addMethod(_method);
            }
        }
    }
}
