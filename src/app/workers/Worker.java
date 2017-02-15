/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.beans.OtherWord;
import app.helpers.ProgressDialog;
import app.workers.config.ConfigWorker;
import java.io.File;
import java.io.StringWriter;
import static java.lang.ProcessBuilder.Redirect.to;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
    private ArrayList<OtherWord> otherwords;

    Document doc;
    private HashMap<Eleve, String> elevesHtml;

    public Worker(ConfigWorker conf) {
        modules = new ArrayList<>(conf.getModules());
        eleves = new ArrayList<>(conf.getEleves());
        elevesHtml = new HashMap<>();
        otherwords = new ArrayList<>(conf.getOthersWords());
        try {
            doc = doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        ;
    }

    public void init() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int progressMax = eleves.size() * 2;
                int progress = 0;
                updateProgress(progress, progressMax);
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
        ProgressDialog dialog = new ProgressDialog(task);
        dialog.showAndWait();
    }

    private Element addToXML(Document doc, Eleve eleveBean) {
        Element eleve = doc.createElement("Eleve");
        eleve.setAttribute("name", eleveBean.getName());
        for (Module moduleBean : modules) {
            Element module = doc.createElement("Categorie");
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
        Element otherword = doc.createElement("Categorie");
        eleve.appendChild(otherword);
        otherword.setAttribute("name", "Autres");
        for (OtherWord word : otherwords) {
            Element keyword = doc.createElement("Keyword");
            otherword.appendChild(keyword);
            keyword.setAttribute("name", word.getName().replaceAll("[\\/>]", ""));
            keyword.setAttribute("present", Boolean.toString(eleveBean.hasKeyword(new Module("Ce string n'est pas censé apparaitre"), word.getName())));
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

    public ArrayList<Eleve> getEleves() {
        return eleves;
    }

    public HashMap<Eleve, String> getElevesHtml() {
        return elevesHtml;
    }

    public void sendMails(String adresse, String serveur) {
        try {
            Properties p = System.getProperties();
            Session s = Session.getDefaultInstance(p);
            p.setProperty("mail.smtp.host", serveur);
            MimeMessage m = new MimeMessage(s);
            m.setFrom(new InternetAddress(adresse));
            m.setSubject("Rapport de dossier de formation");
            for (Eleve eleve : eleves) {
                m.setRecipient(Message.RecipientType.TO, new InternetAddress(eleve.getName() + "@studentfr.ch"));
                m.setContent(elevesHtml.get(eleve), "text/html");
                Transport.send(m);
            }
        } catch (MessagingException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur mail");
            alert.setHeaderText("Erreur lors de l'envoie des mails");
        }
    }

    public void saveXml(File path) {
        if (path != null) {
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                Result output = new StreamResult(path);
                Source input = new DOMSource(doc);
                transformer.transform(input, output);
            } catch (TransformerException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
