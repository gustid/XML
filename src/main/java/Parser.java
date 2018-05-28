import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Parser {
    public static void parse(String xsdFile){

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File("/home/sd/Desktop/XML/src/main/java/" +xsdFile));
                Element root ;
                root = doc.getDocumentElement();
                NodeList list = root.getElementsByTagName("xs:element");
                Element firstElement = (Element) list.item(0);
                String firstElementName = firstElement.getAttribute("name");
                String firstElementClassName = firstElementName.substring(0,1).toUpperCase() +
                        firstElementName.substring(1);
                Element secondElement = (Element)list.item(1);
                String secondElementName = secondElement.getAttribute("name");
                String secondElementClassName = secondElementName.substring(0,1).toUpperCase() +
                        secondElementName.substring(1);

                PrintWriter writer = new PrintWriter("/home/sd/Desktop/XML/src/main/java/"+
                        firstElementClassName+".java");
                writer.println("import java.util.*;");
                writer.println(" ");
                writer.println("public class " + firstElementClassName
                        + " {");
                if(secondElement.getAttribute("maxOccurs").equals("unbounded"))
                {
                    writer.println("protected List<" + secondElementClassName +
                            "> " + firstElementName + ";");
                    //getDots
                    writer.println("public List<" + secondElementClassName +
                            "> get" + firstElementClassName + "(){");
                    writer.println("if( " + firstElementName + " == null ) {");
                    writer.println(firstElementName + " = new ArrayList<" +
                            secondElementClassName + ">();");
                    writer.println("}");

                    writer.println("return this." + firstElementName + ";");
                    writer.println("}");
                    //setDots
                    writer.println("public void set" + firstElementClassName + "("+
                            "List<" + secondElementClassName +
                            "> l" + "){");
                    writer.println(firstElementName + " = l ;");



                    writer.println("}");

                }
                writer.println("}");

                writer.close();

                PrintWriter secondWriter = new PrintWriter(new File("/home/sd/Desktop/XML/src/main/java/"+
                        secondElementClassName+".java"));
                secondWriter.println("import java.util.*;");
                secondWriter.println(" ");
                secondWriter.println("public class " + secondElementClassName
                        + " {");

                NodeList attributesList = secondElement.getElementsByTagName("xs:attribute");
                int len = attributesList.getLength();
                for(int i=0;i<len;i++)
                {
                    Element e = (Element)attributesList.item(i);
                    String eName = e.getAttribute("name");
                    String eType = e.getAttribute("type");
                    String tmp = eType.substring(3);

                    if(new String("intdoublefloatchar").contains(tmp))
                    {
                        eType = tmp;
                    }else{
                        eType = tmp.substring(0,1).toUpperCase() +
                                tmp.substring(1);
                    }


                    secondWriter.println("protected " + eType + " " +eName + ";");
                }

                for(int i=0;i<len;i++)
                {
                    Element e = (Element)attributesList.item(i);
                    String eName = e.getAttribute("name");
                    String eType = e.getAttribute("type");
                    String tmp = eType.substring(3);

                    if(new String("intdoublefloatchar").contains(tmp))
                    {
                        eType = tmp;
                    }else{
                        eType = tmp.substring(0,1).toUpperCase() +
                                tmp.substring(1);
                    }


                    secondWriter.println("public void set" +eName + " ( " + eType + " value ) {");
                    secondWriter.println("this." + eName + " = value ; ");
                    secondWriter.println("}");
                }

                for(int i=0;i<len;i++)
                {
                    Element e = (Element)attributesList.item(i);
                    String eName = e.getAttribute("name");
                    String eType = e.getAttribute("type");
                    String tmp = eType.substring(3);

                    if(new String("intdoublefloatchar").contains(tmp))
                    {
                        eType = tmp;
                    }else{
                        eType = tmp.substring(0,1).toUpperCase() +
                                tmp.substring(1);
                    }


                    secondWriter.println("public  " + eType +" get" +eName + " ( ) {");
                    secondWriter.println("return this." + eName + "; ");
                    secondWriter.println("}");
                }

                secondWriter.println("}");



                secondWriter.close();



            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public static String pathToFirst(String xsdFile){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String firstElementClassName = null;

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("/home/sd/Desktop/XML/src/main/java/" +xsdFile));
            Element root ;
            root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("xs:element");
            Element firstElement = (Element) list.item(0);
            String firstElementName = firstElement.getAttribute("name");
            firstElementClassName = firstElementName.substring(0,1).toUpperCase() +
                    firstElementName.substring(1);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstElementClassName;

    }

    public static String pathToSecond(String xsdFile){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String firstElementClassName = null;
        String secondElementClassName = null;


        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("/home/sd/Desktop/XML/src/main/java/" +xsdFile));
            Element root ;
            root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("xs:element");
            Element firstElement = (Element) list.item(0);
            String firstElementName = firstElement.getAttribute("name");
            firstElementClassName = firstElementName.substring(0,1).toUpperCase() +
                    firstElementName.substring(1);

            Element secondElement = (Element)list.item(1);
            String secondElementName = secondElement.getAttribute("name");
            secondElementClassName = secondElementName.substring(0,1).toUpperCase() +
                    secondElementName.substring(1);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secondElementClassName;

    }




}
