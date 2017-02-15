/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers.config;

import app.beans.Eleve;
import app.beans.Module;
import app.beans.OtherWord;
import java.io.File;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jordan
 */
public class Config implements Serializable {

    private ArrayList<Eleve> eleves;
    private ArrayList<Eleve> ignoredEleves;
    private HashMap<String, HashMap<String, Boolean>> modules;
    private HashMap<String, HashMap<String, Boolean>> ignoredModules;
    private HashMap<String, Boolean> otherWords;
    private String folderPath;
    private File elevesPath;

    public Config(List<Eleve> eleves, List<Eleve> ignoredEleves, List<Module> modules,
            List<Module> ignoredModules,List<OtherWord> otherWords, String folderPath, File elevesPath) {

        this.otherWords = new HashMap<>();
        this.eleves = new ArrayList<>(eleves);
        this.ignoredEleves = new ArrayList<>(ignoredEleves);
        this.folderPath = folderPath;
        this.elevesPath = elevesPath;
        this.modules = new HashMap<>();
        this.ignoredModules = new HashMap<>();
        
        if(folderPath == null){
            this.folderPath = "";
        }
        
        for (Module module : ignoredModules) {
            HashMap<String, Boolean> bools = new HashMap<>();
            for (String keyword : module.getKeywords().keySet()) {
                bools.put(keyword, module.getKeywords().get(keyword).getValue());
            }
            this.ignoredModules.put(module.toString(), bools);
        }

        for (Module module : modules) {
            HashMap<String, Boolean> bools = new HashMap<>();
            for (String keyword : module.getKeywords().keySet()) {
                bools.put(keyword, module.getKeywords().get(keyword).getValue());
            }
            this.modules.put(module.toString(), bools);
        }
        
        for (OtherWord word : otherWords) {
            this.otherWords.put(word.getName(), word.getActive().getValue());
        }
    }

    public ArrayList<Eleve> getEleves() {
        return eleves;
    }

    public ObservableList<Eleve> getIgnoredEleves() {
        return FXCollections.observableArrayList(ignoredEleves);
    }

    public ObservableList<Module> getModules() {
       return unserializeModule(modules);
    }

    public ObservableList<Module> getIgnoredModules() {
        return unserializeModule(ignoredModules);
    }

    public String getFolderPath() {
        return folderPath;
    }

    public File getElevesPath() {
        return elevesPath;
    }

    public ArrayList<OtherWord> getOtherWords() {
        ArrayList<OtherWord> res = new ArrayList<>();
        for (String s : otherWords.keySet()) {
            res.add(new OtherWord(s, new SimpleBooleanProperty(otherWords.get(s))));
        }
        return res;
    }

    private ObservableList<Module> unserializeModule(HashMap<String, HashMap<String, Boolean>> modules){
        ObservableList<Module> res = FXCollections.observableArrayList();
        for (String module : modules.keySet()) {
            res.add(new Module(module, modules.get(module)));
        }
        return res;
    }
}
