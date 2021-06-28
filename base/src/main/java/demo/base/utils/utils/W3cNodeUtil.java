package demo.base.utils.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class W3cNodeUtil {
    public static String node2XmlStr(Node node) {
        Transformer transformer = null;
        if (node == null) {
            throw new IllegalArgumentException("node 不能为空..");
        }
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (transformer != null) {
            try {
                StringWriter sw = new StringWriter();
                transformer.transform(new DOMSource(node), new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException te) {
                throw new RuntimeException(te.getMessage());
            }
        }
        return null;
    }


    public static String xml2Xml(String xml, Source XSLSource) {
        Transformer transformer = null;
        if (xml == null) {
            throw new IllegalArgumentException("node 不能为空..");
        }
        try {
            if (XSLSource == null) {
                transformer = TransformerFactory.newInstance().newTransformer();
            } else {
                transformer = TransformerFactory.newInstance().newTransformer(XSLSource);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (transformer != null) {
            try {
                Source source = new StreamSource(new StringReader(xml));
                StringWriter sw = new StringWriter();
                transformer.transform(source, new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException te) {
                throw new RuntimeException(te.getMessage());
            }
        }
        return null;
    }

    public static Document xmlStr2Node(String xmlString, String encoding) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            InputStream is = new ByteArrayInputStream(xmlString.getBytes(encoding));
            doc = dbf.newDocumentBuilder().parse(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static Node getChildNode(Node node, String nodeName) {
        if (!node.hasChildNodes()) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (nodeName.equals(childNode.getNodeName())) {
                return childNode;
            }
            childNode = getChildNode(childNode, nodeName);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    public static Node getChildChainNode(Node node, String... nodeName) {
        Node childNode = node;
        for (int i = 0; i < nodeName.length; i++) {
            String tmp = nodeName[i];
            childNode = getChildNode(childNode, tmp);
            if (childNode == null) {
                return null;
            }
        }
        return childNode;
    }
}
