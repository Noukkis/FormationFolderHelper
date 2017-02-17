/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Jordan
 */
public class Eleve implements Serializable {

    String name;
    File directory;
    ArrayList<String> arborescence;
    String color;
    String html;

    public Eleve(String name, File directory) {
        this.name = name;
        this.directory = directory;
        color = "black";
        html = "";
    }

    public boolean hasKeyword(Module module, String keyword) {
        if (arborescence == null) {
            arborescence = new ArrayList<>();
            listFiles(directory);
        }
        if (keyword != null && keyword.length() > 0) {
            switch (keyword.charAt(0)) {
                case '/':
                    for (String s : arborescence) {
                        if (s.matches(".*" + module.toString().toLowerCase() + "\\\\" + keyword.replace("/", "").toLowerCase() + "\\\\.*")) {
                            return true;
                        }
                    }
                    break;
                case '>':
                    for (String s : arborescence) {
                        if (s.contains(keyword.replace(">", ""))) {
                            return true;
                        }
                    }
                    break;
                default:
                    for (String s : arborescence) {
                        if (s.matches(".*" + module.toString().toLowerCase() + "\\\\descriptif\\\\.*" + keyword + ".*")) {
                            return true;
                        }
                    }
            }
        }
        return false;
    }

    public void listFiles(File dir) {
        arborescence.add(dir.getAbsolutePath().replace(directory.getAbsolutePath(), "").toLowerCase());
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                listFiles(file);
            }
        }
    }
    
    public void clearFiles(){
        arborescence = null;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    
}
