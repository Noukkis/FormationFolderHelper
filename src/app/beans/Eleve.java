/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Jordan
 */
public class Eleve implements Serializable{
    
    String name;
    File directory;

    public Eleve(String name, File directory) {
        this.name = name;
        this.directory = directory;
    }
    
    @Override
    public String toString(){
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
}
