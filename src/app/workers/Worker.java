/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.ihms.ConfigViewCtrl;
import app.ihms.ProgressCtrl;
import app.ihms.ViewCtrl;
import app.workers.config.ConfigWorker;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Jordan
 */
public class Worker {

    private ArrayList<Module> modules;
    private ArrayList<Eleve> eleves;

    private HashMap<Eleve, String> elevesHtml;

    public Worker(ConfigWorker conf) {
        modules = new ArrayList<>(conf.getModules());
        eleves = new ArrayList<>(conf.getEleves());
        elevesHtml = new HashMap<>();
    }

    public Task init() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int progressMax = eleves.size() * 2;
                int progress = 0;
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element root = doc.createElement("Eleves");
                doc.appendChild(root);
                for (Eleve eleveBean : eleves) {
                    root.appendChild(addToXML(doc, eleveBean));
                    progress++;
                    updateProgress(progress, progressMax);
                    updateMessage("Création du fichier XML (" + progress + "/" + eleves.size() + ")");
                }
                for (Eleve eleve : eleves) {
                    progress++;
                    updateProgress(progress, progressMax);
                    updateMessage("Transformations XSLT (" + (progress - eleves.size()) + "/" + eleves.size() + ")");
                    elevesHtml.put(eleve, transform(doc, eleve.getName()));
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.start();
        return task;
    }

    private Element addToXML(Document doc, Eleve eleveBean) {
        Element eleve = doc.createElement("Eleve");
        eleve.setAttribute("name", eleveBean.getName());
        for (Module moduleBean : modules) {
            Element module = doc.createElement("Module");
            eleve.appendChild(module);
            module.setAttribute("name", moduleBean.toString());
            for (String s : moduleBean.getKeywords().keySet()) {
                if (moduleBean.getKeywords().get(s).getValue()) {
                    Element keyword = doc.createElement("Keyword");
                    module.appendChild(keyword);
                    keyword.setAttribute("name", s.replaceAll("[\\/>]", ""));
                    keyword.setAttribute("present", Boolean.toString(eleveBean.hasKeyword(moduleBean, s)));
                }
            }
        }
        return eleve;
    }

    private String transform(Document doc, String eleveName) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource("resources/template.xsl"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setParameter("eleveName", eleveName);
        StringWriter res = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(res));
        return res.toString();
    }

}
