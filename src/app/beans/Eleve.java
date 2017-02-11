/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Jordan
 */
public class Eleve implements Serializable {

    String name;
    File directory;

    public Eleve(String name, File directory) {
        this.name = name;
        this.directory = directory;
    }

    public boolean hasKeyword(Module module, String keyword) {
        ArrayList<File> files = listFiles(directory);
        switch (keyword.charAt(0)) {
            case '/':
                for (File folder : files) {
                    if (folder.getAbsolutePath().endsWith("\\" + module.toString() + keyword.replace("/", "\\")) && folder.list() != null && folder.list().length > 0) {
                        return true;
                    }
                }
                break;
            case '>':
                for (File folder : files) {
                    if (folder.getAbsolutePath().contains(keyword.replace(">", ""))) {
                        return true;
                    }
                }
                break;
            default:
                for (File folder : files) {
                    if (folder.getAbsolutePath().endsWith("\\" + module.toString() + "\\Descriptif")) {
                        for (File file : listFiles(folder)) {
                            if (file.getName().contains(keyword)) {
                                return true;
                            }
                        }
                    }
                }
        }
        return false;
    }

    public ArrayList<File> listFiles(File mainDir) {
        ArrayList<File> res = new ArrayList<>();
        if (mainDir.isDirectory()) {
            res.addAll(Arrays.asList(mainDir.listFiles()));
            ArrayList<File> subs = new ArrayList<>();
            for (File file : res) {
                subs.addAll(listFiles(file));
            }
            res.addAll(subs);
        }
        return res;
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
}
