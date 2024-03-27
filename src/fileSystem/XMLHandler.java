package fileSystem;

import controller.ElementBuilderHelper;
import controller.exceptions.WrongDataInputException;
import controller.exceptions.WrongTagException;
import lombok.Getter;
import lombok.Setter;
import model.LinkedListStorage;
import model.data.StudyGroup;
import model.interfaces.IStore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * XMLHandler handles xml file and get it's input.
 */
public class XMLHandler {
    @Getter
    @Setter
    private static Node node;
    public static IStore<StudyGroup> getStorageFromXML() throws Throwable, IOException, SAXException, WrongDataInputException, ReflectiveOperationException, WrongTagException, ParserConfigurationException {
        File file = new File(ScriptHandler.getFileName());
        if (file.toString().equals("")){
            return new LinkedListStorage<StudyGroup>();
        }
        Scanner scan = new Scanner(file);
        if (!scan.nextLine().startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>")){
            throw new WrongTagException("Invalid version tag");
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        documentBuilder.setErrorHandler(new NullErrorHandler());
        Document doc = documentBuilder.parse(file);
        IStore<StudyGroup> storage = new LinkedListStorage<>();
        node = doc.getFirstChild();
        NodeList rootChildren = node.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++){
            if (rootChildren.item(i).getNodeType() == Node.ELEMENT_NODE){
                storage.add(ElementBuilderHelper.buildElement(StudyGroup.class, "studyGroup", ""));
            }
        }
        //System.out.println(node);
        return storage;
    }


    public static Node getNextNode(){
        //System.out.println(node);
        node = getNextNode(node);
        return node;
    }
    public static String getValue(){
        return node.getTextContent();
    }
    private static Node getElementSiblingNode(Node node){
        do {
            if (node.getNextSibling() == null) return node;
            node = node.getNextSibling();
        } while (node.getNodeType() != Node.ELEMENT_NODE);
        return node;
    }
    private static Node getChildNode(Node node){
        if (node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) return node.getFirstChild();
        return getElementSiblingNode(node.getFirstChild());
    }
    private static boolean isLastSibling(Node node, Node nextNode){
        return (nextNode.getNextSibling() == null && nextNode.getNodeType() != Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() == Node.ELEMENT_NODE);
    }

    public static Node getNextNode(Node node){
        if (node.getChildNodes().getLength() > 1){
            return getChildNode(node);
        }
        Node nextNode = getElementSiblingNode(node);
        if ((nextNode.getNextSibling() == null && nextNode.getNodeType() != Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() == Node.ELEMENT_NODE)){
            Node parNode;
            do{
                //System.out.println(node);
                parNode = node.getParentNode();
                node = getElementSiblingNode(parNode);
                //System.out.println(node + " " + getElementSiblingNode(node) + " " + node.getNextSibling() + " " + (nextNode.getNextSibling() == null && nextNode.getNodeType() != Node.ELEMENT_NODE) + " " + (node.getNextSibling() == null && node.getNodeType() == Node.ELEMENT_NODE) + " " + isLastSibling(node, getElementSiblingNode(node)));
            } while ((parNode.equals(node) && node.getNodeType() == Node.ELEMENT_NODE) || (node.getNextSibling() == null && node.getNodeType() != Node.ELEMENT_NODE));
            //System.out.println(node + " " + node.getNextSibling());
            return node;
        }
        node = nextNode;
        return node;
        //System.out.println(node);
    }

}
