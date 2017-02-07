package app.workers;

import app.beans.Eleve;
import app.beans.Module;
import app.ihms.DoubleListCtrl;
import app.ihms.ElevesController;
import app.ihms.KeywordsController;
import app.ihms.TabController;
import app.ihms.ViewController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

/**
 * Cette interface définit les services de la couche "métier" de l'application.
 *
 * @author ...
 * @version 1.0 / date
 */
public class ConfigWorker {

    private ArrayList<Module> modules;
    private ArrayList<Module> ignoredModules;
    private ArrayList<String> keywords;
    private ArrayList<Eleve> eleves;
    private ArrayList<Eleve> ignoredEleves;

    private HashMap<String, TabController> ctrls;

    public ConfigWorker() {
        modules = new ArrayList<>();
        eleves = new ArrayList<>();
        keywords = new ArrayList<>();
        ctrls = new HashMap<>();
        ignoredEleves = new ArrayList<>();
        ignoredModules = new ArrayList<>();
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

    public ArrayList<Eleve> updateEleves(File elevesPath, String folderPath) {
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
        ElevesController ctrl = (ElevesController) ctrls.get("Eleves");
        ctrl.update();
        return eleves;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public ArrayList<Eleve> getEleves() {
        return eleves;
    }

    public HashMap<String, TabController> getCtrls() {
        return ctrls;
    }

    public void saveConfig(File savePath, File elevesPath, String folderPath) {
        if (savePath != null) {
            try {
                FileOutputStream fos = new FileOutputStream(savePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ElevesController elevesCtrl = (ElevesController) ctrls.get("Eleves");
                DoubleListCtrl modulesCtrl = (DoubleListCtrl) ctrls.get("Modules");
                Config config = new Config(elevesCtrl.getLstLeft().getItems(), elevesCtrl.getLstRight().getItems(),
                        modules, modulesCtrl.getLstRight().getItems(), folderPath, elevesPath);
                oos.writeObject(config);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadConfig(File savePath, ViewController viewCtrl) {
        if (savePath != null) {
            try {
                FileInputStream fis = new FileInputStream(savePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Config config = (Config) ois.readObject();
                ois.close();
                fis.close();
                loadConfig(config, viewCtrl);
            } catch (IOException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Erreur à l'ouverture du fichier");
                 alert.setHeaderText("Le fichier n'a pas pu être ouvert. Il est peut-être corrompu");
                 alert.showAndWait();
            }
        }
    }

    private void loadConfig(Config config, ViewController viewCtrl) {
        ElevesController elevesCtrl = (ElevesController) ctrls.get("Eleves");
        DoubleListCtrl modulesCtrl = (DoubleListCtrl) ctrls.get("Modules");
        KeywordsController keywordsCtrl = (KeywordsController) ctrls.get("Keywords");
        modules = config.getModules();
        eleves = config.getAllEleves();
        viewCtrl.setElevesPath(config.getElevesPath());
        viewCtrl.setFolderPath(config.getFolderPath());
        viewCtrl.update();
        elevesCtrl.update();
        modulesCtrl.update();
        elevesCtrl.toRight(config.getIgnoredEleves());
        modulesCtrl.toRight(config.getIgnoredModules(modules));
        keywordsCtrl.init(this);
    }
}
