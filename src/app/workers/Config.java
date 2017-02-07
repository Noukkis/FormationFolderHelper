/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.ihms.ElevesController;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Jordan
 */
public class Config implements Serializable {

    private ArrayList<Eleve> eleves;
    private ArrayList<Eleve> ignoredEleves;
    private HashMap<String, HashMap<String, Boolean>> modules;
    private ArrayList<String> ignoredModules;
    private String folderPath;
    private File elevesPath;

    public Config(List<Eleve> eleves, List<Eleve> ignoredEleves, List<Module> modules,
            List<Module> ignoredModules, String folderPath, File elevesPath) {

        this.eleves = new ArrayList<>(eleves);
        this.ignoredEleves = new ArrayList<>(ignoredEleves);
        this.folderPath = folderPath;
        this.elevesPath = elevesPath;
        this.modules = new HashMap<>();
        this.ignoredModules = new ArrayList<>();

        for (Module module : ignoredModules) {
            this.ignoredModules.add(module.toString());
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

    public ArrayList<Eleve> getIgnoredEleves() {
        return ignoredEleves;
    }

    public ArrayList<Module> getModules() {
        ArrayList<Module> res = new ArrayList<>();
        for (String name : modules.keySet()) {
            res.add(new Module(name, modules.get(name)));
        }
        return res;
    }

    public ArrayList<Module> getIgnoredModules(ArrayList<Module> modules) {
        ArrayList<Module> res = new ArrayList<>();
        for (Module module : modules) {
            if (ignoredModules.contains(module.toString())) {
                res.add(module);
            }
        }
        return res;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public File getElevesPath() {
        return elevesPath;
    }

    public ArrayList<Eleve> getAllEleves() {
        ArrayList<Eleve> res = new ArrayList<>(eleves);
        res.addAll(ignoredEleves);
        return res;
    }

}
