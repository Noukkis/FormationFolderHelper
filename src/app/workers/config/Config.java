/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers.config;

import app.beans.Eleve;
import app.beans.Module;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String folderPath;
    private File elevesPath;

    public Config(List<Eleve> eleves, List<Eleve> ignoredEleves, List<Module> modules,
            List<Module> ignoredModules, String folderPath, File elevesPath) {

        this.eleves = new ArrayList<>(eleves);
        this.ignoredEleves = new ArrayList<>(ignoredEleves);
        this.folderPath = folderPath;
        this.elevesPath = elevesPath;
        this.modules = new HashMap<>();
        this.ignoredModules = new HashMap<>();

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

    private ObservableList<Module> unserializeModule(HashMap<String, HashMap<String, Boolean>> modules){
        ObservableList<Module> res = FXCollections.observableArrayList();
        for (String module : modules.keySet()) {
            res.add(new Module(module, modules.get(module)));
        }
        return res;
    }
}
