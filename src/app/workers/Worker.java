/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.beans.OtherWord;
import app.workers.config.ConfigWorker;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Jordan
 */
public class Worker {

    private ArrayList<Module> modules;
    private ObservableList<Eleve> eleves;
    private ArrayList<OtherWord> otherwords;

    Document doc;
    Element root;
    private HashMap<Eleve, String> elevesHtml;

    public Worker(ConfigWorker conf) {
        modules = new ArrayList<>(conf.getModules());
        eleves = FXCollections.observableArrayList(conf.getEleves());
        elevesHtml = new HashMap<>();
        otherwords = new ArrayList<>(conf.getOthersWords());
        try {
            doc = doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            root = doc.createElement("Eleves");
            doc.appendChild(root);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        ;
    }

    public void init() {
        for (Eleve eleve : eleves) {
            Thread t = new Thread(new EleveVerifier(eleve, this));
            t.start();
        }
    }

    public void addToXML(Element eleve, Eleve eleveBean) {
        try {
            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();
            if (path.evaluate("//Eleve[@name='" + eleveBean.getName() + "']", doc, XPathConstants.NODE) != null) {
                root.removeChild((Node) path.evaluate("//Eleve[@name='" + eleveBean.getName() + "']", doc, XPathConstants.NODE));
            }
            eleveBean.setColor("blue");
            root.appendChild(eleve);
            elevesHtml.put(eleveBean, transform(doc, eleveBean.getName()));
        } catch (XPathExpressionException | TransformerException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public ObservableList<Eleve> getEleves() {
        return eleves;
    }

    public HashMap<Eleve, String> getElevesHtml() {
        return elevesHtml;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Document getDoc() {
        return doc;
    }

    public ArrayList<OtherWord> getOtherwords() {
        return otherwords;
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
