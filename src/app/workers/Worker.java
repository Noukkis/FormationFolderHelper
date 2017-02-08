/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.workers.config.ConfigWorker;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Jordan
 */
public class Worker {

    private ArrayList<Module> modules;
    private ArrayList<Eleve> eleves;

    public Worker(ConfigWorker conf) {
        modules = new ArrayList<>(conf.getModules());
        eleves = new ArrayList<>(conf.getEleves());
    }

    public void makeXML() throws FileNotFoundException, TransformerException {
        try {
           Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
           Element root = doc.createElement("Modules");
           doc.appendChild(root);
            for (Module module : modules) {
                Element moduleElement = doc.createElement("Module");
                moduleElement.setAttribute("nom", module.toString());
                root.appendChild(moduleElement);
                for (String keyword : module.getKeywords().keySet()) {
                    moduleElement.setAttribute(keyword, module.getKeywords().get(keyword).getValue().toString());
                }
            }
            
             TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source s = new DOMSource(doc);
            Result res = new StreamResult( new FileOutputStream("test.xml"));
            transformer.transform(s, res);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
