/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.beans.OtherWord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Jordan
 */
public class EleveVerifier implements Runnable{

    Eleve eleveBean;
    Worker wrk;

    public EleveVerifier(Eleve eleveBean, Worker wrk) {
        this.eleveBean = eleveBean;
        this.wrk = wrk;
    }

    @Override
    public void run() {
        Document doc = wrk.getDoc();
        Element eleve = doc.createElement("Eleve");
        eleve.setAttribute("name", eleveBean.getName());
        for (Module moduleBean : wrk.getModules()) {
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
        for (OtherWord word : wrk.getOtherwords()) {
            Element keyword = doc.createElement("Keyword");
            otherword.appendChild(keyword);
            keyword.setAttribute("name", word.getName().replaceAll("[\\/>]", ""));
            keyword.setAttribute("present", Boolean.toString(eleveBean.hasKeyword(new Module("Ce string n'est pas censé apparaitre"), word.getName())));
        }
        
        wrk.addToXML(eleve, eleveBean);
    }
    
    @Override
    public String toString(){
        return eleveBean.toString();
    }
    
}

