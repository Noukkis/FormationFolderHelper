package app.ihms.config;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Vue principale
 * @author P-A Mettraux
 */
public class View extends Application {
  
  @Override
  public void start(Stage stage) throws Exception {
    //Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
    FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
    Parent root = (Parent) loader.load();
    
    // Récupère le controleur
    ViewCtrl ctrl = loader.getController();
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("FFH");
    stage.show();
    
    stage.setOnCloseRequest(e -> ctrl.quitter());

  }

}
