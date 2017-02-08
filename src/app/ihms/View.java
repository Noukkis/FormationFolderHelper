package app.ihms;

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
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfigView.fxml"));
    Parent root = (Parent) loader.load();
    
    // Récupère le controleur
    ConfigViewCtrl ctrl = loader.getController();
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("FFH");
    stage.show();
    
    stage.setOnCloseRequest(e -> ctrl.quitter());

  }
}
