package de.foopara.phpcsmd.exec.phpmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.foopara.phpcsmd.debug.Logger;
import de.foopara.phpcsmd.generics.GenericOutputReader;
import de.foopara.phpcsmd.generics.GenericViolation;
import de.foopara.phpcsmd.option.phpmd.GenericPhpmdSniffRegistry;

/**
 *
 * @author nspecht
 */
public class PhpmdXMLParser
{

    public PhpmdResult parse(GenericOutputReader reader, String fileFilter) {
        List<GenericViolation> violations = new ArrayList<GenericViolation>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.parse(new InputSource(reader.getReader()));
            if (fileFilter != null) {
                fileFilter = fileFilter.toLowerCase().replace("\\", "/").trim();
                NodeList files = document.getElementsByTagName("file");
                for (int i = 0; i< files.getLength(); i++) {
                    String file = files.item(i).getAttributes().getNamedItem("name").getTextContent().toLowerCase().replace("\\", "/").trim();
                    if (file.contains(fileFilter) || fileFilter.contains(file)) {
                        for(int j = 0; j < files.item(i).getChildNodes().getLength(); j++) {
                            this.addVioloation(files.item(i).getChildNodes().item(j), violations);
                        }
                    }
                }
            } else {
                NodeList ndList = document.getElementsByTagName("violation");

                for (int i = 0; i < ndList.getLength(); i++) {
                    Node node = ndList.item(i);
                    this.addVioloation(node, violations);
                }
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

    protected void addVioloation(Node node, List<GenericViolation> violations) {
        if (node == null || violations == null) {
            return;
        }
        String message = node.getTextContent().trim();
        NamedNodeMap nm = node.getAttributes();
        if (nm == null || nm.getNamedItem("beginline") == null || nm.getNamedItem("endline") == null) {
            return;
        }
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
}
