import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;


public class Library {
    public static Object createFromFile(String path) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = db.parse(new File("/home/sd/Desktop/XML/src/main/java/" +path+".xml"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root ;
        root = doc.getDocumentElement();
        String pathToXSD = root.getAttribute("noNamespaceSchemaLocation");
        Parser.parse(pathToXSD);

        // compile the dot source file


        File dFile = new File("/home/sd/Desktop/XML/src/main/java/" +
                Parser.pathToSecond(pathToXSD) + ".java");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File parentDirectory = dFile.getParentFile();
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(dFile));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

        fileManager.close();

        // load the compiled dot class
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { parentDirectory.toURI().toURL() });
        String dClassName = Parser.pathToSecond(pathToXSD);
        Class<?> dClass = classLoader.loadClass(dClassName);

        //compile the dots file

        File dsFile = new File("/home/sd/Desktop/XML/src/main/java/" +
                Parser.pathToFirst(pathToXSD) + ".java");
        compiler = ToolProvider.getSystemJavaCompiler();
        fileManager = compiler.getStandardFileManager(null, null, null);
        parentDirectory = dFile.getParentFile();
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
        compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(dFile));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

        fileManager.close();

        // load the compiled dot class
        classLoader = URLClassLoader.newInstance(new URL[] { parentDirectory.toURI().toURL() });
        String dsClassName = Parser.pathToFirst(pathToXSD);
        Class<?> dsClass = classLoader.loadClass(dsClassName);






        List l = new ArrayList();
        NodeList nl = root.getElementsByTagName(Parser.pathToSecond(pathToXSD));
        int len = nl.getLength();
        for(int i =0 ; i<len;i++)
        {
            Element e = (Element)nl.item(i);
            Field[] dFields =  dClass.getDeclaredFields();
            Object d =null ;
            try {
                 d = dClass.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            for(int j=0;j<dFields.length;j++ )
            {
                String fieldName = dFields[i].getName();
                String value = e.getAttribute(fieldName);
                String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                Method m = dClass.getDeclaredMethod(methodName);
                Object invoke = m.invoke(d,Integer.parseInt(value));

            }
            l.add(d);
        }

        Object ds = null;
        ds = dsClass.newInstance();
        //System.out.println(dsClass.getDeclaredMethods()[1].getName());
        //Method m = dsClass.getDeclaredMethod("set"+Parser.pathToFirst(pathToXSD));

        dsClass.getDeclaredMethods()[1].invoke(ds,l);
        return ds;




    }
}
