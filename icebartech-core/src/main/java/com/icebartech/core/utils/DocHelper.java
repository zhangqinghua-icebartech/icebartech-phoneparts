package com.icebartech.core.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * xml解析工具类
 *
 * @author Administrator
 */
public class DocHelper {

    public static final String CHARACTER_ENCODING = "utf-8";

    private Document doc;

    public DocHelper(String data) {
        doc = convertToXML(data);
    }

    public DocHelper(String data, String encoding) {
        doc = convertToXML(data, encoding);
    }

    public String getNodeValue(String tag) {

        NodeList nodeList = doc.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) {
            return null;
        }
        return getNodeValue(tag, 0);
    }

    public String getNodeValue(String tag, int index) {
        if (doc.getElementsByTagName(tag).getLength() == 0) {
            return null;
        }

        Node node = doc.getElementsByTagName(tag).item(index).getFirstChild();
        if (node == null) {
            return "";
        }
        return node.getNodeValue();
    }

    public int getNodeCount(String tag) {

        return doc.getElementsByTagName(tag).getLength();
    }

    public Document getDoc() {
        return doc;
    }

    public static Document convertToXML(String data, String encoding) {

        StringBuilder sXML = new StringBuilder();
        sXML.append(data);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            InputStream is = new ByteArrayInputStream(sXML.toString().getBytes(encoding));
            doc = dbf.newDocumentBuilder().parse(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document convertToXML(String data) {
        return convertToXML(data, CHARACTER_ENCODING);
    }
}
