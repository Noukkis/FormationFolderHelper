package app.workers.config;

import app.beans.Eleve;
import app.beans.Module;
import app.ihms.View;
import app.ihms.ConfigViewCtrl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Cette interface définit les services de la couche "métier" de l'application.
 *
 * @author ...
 * @version 1.0 / date
 */
public class ConfigWorker {

    private ObservableList<Module> modules;
    private ObservableList<Module> ignoredModules;
    private ArrayList<String> keywords;
    private ObservableList<Eleve> eleves;
    private ObservableList<Eleve> ignoredEleves;

    private File elevesPath;
    private String folderPath;

    public ConfigWorker() {
        modules = FXCollections.observableArrayList();
        eleves = FXCollections.observableArrayList();
        keywords = new ArrayList<>();
        ignoredEleves = FXCollections.observableArrayList();
        ignoredModules = FXCollections.observableArrayList();
    }

    public void init() {
        makeModules("./resources/modulesList.txt", "./resources/keywordsList.txt");
    }

    private boolean makeModules(String pathModules, String pathKeywords) {
        try {
            Scanner readModules = new Scanner(new File(pathModules));
            while (readModules.hasNext()) {
                modules.add(new Module(readModules.nextLine()));
            }

            keywords = new ArrayList<>();
            Scanner readKeywords = new Scanner(new File(pathKeywords));
            while (readKeywords.hasNext()) {
                keywords.add(readKeywords.nextLine());
            }

        } catch (FileNotFoundException e) {
            return false;
        }
        for (Module module : modules) {
            for (String keyword : keywords) {
                module.getKeywords().put(keyword, new SimpleBooleanProperty(true));
            }
        }
        return true;
    }

    public ObservableList<Eleve> updateEleves(File elevesPath, String folderPath) {
        if (elevesPath != null && elevesPath.isDirectory()) {
            eleves.clear();
            for (File file : elevesPath.listFiles()) {
                if (file.isDirectory()) {
                    File directory = new File(file.getAbsolutePath() + folderPath);
                    if (directory.isDirectory()) {
                        eleves.add(new Eleve(file.getName(), directory));
                    }
                }
            }

            if (eleves.size() < elevesPath.listFiles().length) {
                ListView<String> lstProblemes = new ListView<>();
                ArrayList<String> elevesString = new ArrayList<>();
                for (Eleve eleve : eleves) {
                    elevesString.add(eleve.toString());
                }
                for (File file : elevesPath.listFiles()) {
                    if (!elevesString.contains(file.getName()) && file.isDirectory()) {
                        lstProblemes.getItems().add(file.getName());
                    }
                }
                if (!lstProblemes.getItems().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(lstProblemes.getItems().size() + " répertoires d'élèves ne contiennent pas le repertoire du dossier de formation");
                    alert.getDialogPane().setExpandableContent(lstProblemes);
                    alert.showAndWait();
                }
            }
        }
        return eleves;
    }

    public void saveConfig(File savePath) {
        if (savePath != null) {
            try {
                FileOutputStream fos = new FileOutputStream(savePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                Config config = new Config(eleves, ignoredEleves, modules, ignoredModules, folderPath, elevesPath);
                oos.writeObject(config);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean loadConfig(File savePath) {
        if (savePath != null) {
            try {
                FileInputStream fis = new FileInputStream(savePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Config config = (Config) ois.readObject();
                ois.close();
                fis.close();
                loadConfig(config);
            } catch (IOException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur à l'ouverture du fichier");
                alert.setHeaderText("Le fichier n'a pas pu être ouvert. Il est peut-être corrompu");
                alert.showAndWait();
            }
        } else {
            return false;
        }
        return true;
    }

    private void loadConfig(Config config) {
        modules.setAll(config.getModules());
        eleves.setAll(config.getEleves());
        ignoredModules.setAll(config.getIgnoredModules());
        ignoredEleves.setAll(config.getIgnoredEleves());
        elevesPath = config.getElevesPath();
        folderPath = config.getFolderPath();
    }

    public ObservableList<Module> getModules() {
        return modules;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public ObservableList<Eleve> getEleves() {
        return eleves;
    }

    public ObservableList<Module> getIgnoredModules() {
        return ignoredModules;
    }

    public ObservableList<Eleve> getIgnoredEleves() {
        return ignoredEleves;
    }

    public File getElevesPath() {
        return elevesPath;
    }

    public void setElevesPath(File elevesPath) {
        this.elevesPath = elevesPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}
