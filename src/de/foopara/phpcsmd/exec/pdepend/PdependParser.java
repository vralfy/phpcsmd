/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.exec.pdepend;

import de.foopara.phpcsmd.generics.GenericOutputReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
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

    public PdependResult parse(GenericOutputReader[] readers) {
        PdependResult res = new PdependResult();

        for(GenericOutputReader r : readers) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document;
                document = builder.parse(new InputSource(r.getReader()));

                if (document.getElementsByTagName("metrics").getLength() > 0) {
                    this.parseSummary(document, res);
                } else if (document.getElementsByTagName("PDepend").getLength() > 0) {
                    this.parseJDepend(document, res);
                }
            } catch (IOException ex) {
            } catch (ParserConfigurationException ex) {
            } catch (SAXParseException ex) {
            } catch (SAXException ex) {
            }
        }

        return res;
    }

    private void parseSummary(Document document, PdependResult res) {
        NodeList ndList = document.getElementsByTagName("metrics");
        this.fillMetrics(ndList, res);

        ndList = document.getElementsByTagName("files");
        this.fillFiles(ndList, res);

        ndList = document.getElementsByTagName("package");
        this.fillPackages(ndList, res);

        ndList = document.getElementsByTagName("function");
        this.fillFunctions(ndList, res);
    }

    private void fillValuesByAttributes(Node node, Object o) {
        if (node == null) {
            return;
        }
        if (node.getAttributes() == null) {
            return;
        }
        for(int i=0; i<node.getAttributes().getLength(); i++) {
            Node n = node.getAttributes().item(i);
            this.fillValues(n, n.getTextContent().trim(), o);
        }
    }

    private void fillValuesByChilds(Node node, Object o) {
        if (node == null) {
            return;
        }
        if (node.getChildNodes().getLength() < 1) {
            return;
        }

        for(int i=0; i<node.getChildNodes().getLength(); i++) {
            Node n = node.getChildNodes().item(i);
            if (n.getNodeName().compareTo("#text") != 0) {
                this.fillValues(n, n.getTextContent().trim(), o);
            }
        }
    }

    private void fillValues(Node node, String value, Object o) {
        try {
            Field f = o.getClass().getField(node.getNodeName());
            String type = f.getType().getCanonicalName();

            if (type.compareTo("java.lang.Integer") == 0 || type.compareTo("int") == 0) {
                f.setInt(o, Integer.parseInt(value));
            } else if (type.compareTo("java.lang.Float") == 0 || type.compareTo("float") == 0) {
                f.setFloat(o, Float.parseFloat(value));
            } else if (type.compareTo("java.lang.String") == 0) {
                f.set(o, value + "");

            } else {
                throw new Exception("Unknown type '" + type + "' for field '" + f.getName() + "'");
            }
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void fillMetrics(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            PdependTypes.PdependMetrics _metrics = new PdependTypes.PdependMetrics();
            this.fillValuesByAttributes(ndList.item(i), _metrics);
            res.setMetrics(_metrics);
        }
    }

    private void fillFiles(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            NodeList sub = ndList.item(i).getChildNodes();
            for (int j = 0; j < sub.getLength(); j++) {
                PdependTypes.PdependFile _file = new PdependTypes.PdependFile();
                this.fillValuesByAttributes(sub.item(j), _file);
                res.addFile(_file);
            }
        }
    }

    private void fillPackages(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            String name = ndList.item(i).getAttributes().getNamedItem("name").getNodeValue();
            PdependTypes.PdependPackage _package = res.getPackageInstanceByName(name);
            this.fillValuesByAttributes(ndList.item(i), _package);
            res.addPackage(_package);
            this.fillClasses(ndList.item(i).getChildNodes(), _package);
        }
    }

    private void fillClasses(NodeList ndList, PdependTypes.PdependPackage res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            if (ndList.item(i).getNodeName().compareTo("class") == 0) {
                PdependTypes.PdependClass _class = new PdependTypes.PdependClass();
                this.fillValuesByAttributes(ndList.item(i), _class);
                res.addClass(_class);
                this.fillMethods(ndList.item(i).getChildNodes(), _class);
            }
        }
    }

    private void fillMethods(NodeList ndList, PdependTypes.PdependClass res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            if (ndList.item(i).getNodeName().compareTo("method") == 0) {
                PdependTypes.PdependMethod _method = new PdependTypes.PdependMethod();
                this.fillValuesByAttributes(ndList.item(i), _method);
                res.addMethod(_method);
            } else if (ndList.item(i).getNodeName().compareTo("file") == 0) {
                res.setFilename(ndList.item(i).getAttributes().getNamedItem("name").getNodeValue());
            }
        }
    }

    private void fillFunctions(NodeList ndList, PdependResult res) {
        for (int i = 0; i < ndList.getLength(); i++) {
            if (ndList.item(i).getNodeName().compareTo("function") == 0) {
                PdependTypes.PdependFunction _function = new PdependTypes.PdependFunction();
                this.fillValuesByAttributes(ndList.item(i), _function);
                res.addFunction(_function);

                NodeList sub = ndList.item(i).getChildNodes();
                for (int j = 0; j < sub.getLength(); j++) {
                    if (sub.item(j).getNodeName().compareTo("file") == 0) {
                        _function.setFilename(sub.item(j).getAttributes().getNamedItem("name").getNodeValue());
                    }
                }
            }
        }
    }

    private void parseJDepend(Document document, PdependResult res) {
        NodeList root = document.getElementsByTagName("PDepend").item(0).getChildNodes();
        for(int i=0;i<root.getLength();i++) {
            if (root.item(i).getNodeName().compareTo("Packages") == 0) {
                this.parseJDependPackages(root.item(i).getChildNodes(), res);
            }
        }
    }

    private void parseJDependPackages(NodeList nl, PdependResult res) {
        for(int packId=0;packId<nl.getLength();packId++) {
            if (nl.item(packId).getNodeName().compareTo("#text") != 0) {
                String name = nl.item(packId).getAttributes().getNamedItem("name").getNodeValue();
                PdependTypes.PdependPackage _package = res.getPackageInstanceByName(name);

                for(int section=0;section<nl.item(packId).getChildNodes().getLength();section++) {
                    Node node = nl.item(packId).getChildNodes().item(section);
                    if (node.getNodeName().compareTo("#text") != 0) {
                        if (node.getNodeName().compareTo("Stats") == 0) {
                            this.fillValuesByChilds(
                                    node,
                                    _package
                                    );
                        } else if (node.getNodeName().compareTo("DependsUpon") == 0) {
                            this.parseJDependPackageDependencies(
                                    node.getChildNodes(),
                                    _package.getDepends(),
                                    res
                            );
                        }else if (node.getNodeName().compareTo("UsedBy") == 0) {
                            this.parseJDependPackageDependencies(
                                    node.getChildNodes(),
                                    _package.getUsedBy(),
                                    res
                                    );
                        }
                    }
                }
            }
        }
    }

    private void parseJDependPackageDependencies(NodeList nl, HashSet<PdependTypes.PdependPackage> list, PdependResult res) {
        for(int dep=0;dep<nl.getLength();dep++) {
            if (nl.item(dep).getNodeName().compareTo("#text") != 0) {
                String name = nl.item(dep).getTextContent().trim();
                PdependTypes.PdependPackage p = res.getPackageInstanceByName(name);
                if (!list.contains(p)) {
                    list.add(p);
                }
            }
        }
    }
}
