package app.helpers;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author P-A Mettraux
 */
public class ViewLib {

  /**
   * displayError
   *
   * @param title
   * @param header if null => no header
   * @param erreur
   */
  public static void displayError(String title, String header, String erreur) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(erreur);
    alert.showAndWait();
  }

  /**
   * displayInformation
   *
   * @param title
   * @param header if null => no header
   * @param information
   */
  public static void displayInformation(String title, String header, String information) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(information);
    alert.showAndWait();
  }

  /**
   * displayWarning
   *
   * @param title
   * @param header
   * @param attention
   */
  public static void displayWarning(String title, String header, String attention) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(attention);
    alert.showAndWait();
  }

  /**
   * askConfirmation
   *
   * @param title
   * @param header if null => no header
   * @param message
   * @return true if OK
   */
  public static boolean askConfirmation(String title, String header, String message) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);

    Optional<ButtonType> rep = alert.showAndWait();

    return (rep.get() == ButtonType.OK) ? true : false;
  }

  /**
   * askYesNo
   *
   * @param title
   * @param header if null => no header
   * @param message
   * @param french true => buttons oui/non
   * @return true if OK
   */
  public static boolean askYesNo(String title, String header, String message, boolean french) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);

    ButtonType btnYes = new ButtonType(french ? "Oui" : "Yes");
    ButtonType btnNo = new ButtonType(french ? "Non" : "No");
    alert.getButtonTypes().setAll(btnYes, btnNo);

    Optional<ButtonType> rep = alert.showAndWait();

    return (rep.get() == btnYes) ? true : false;
  }

  /**
   * askInfo
   *
   * @param title
   * @param header if null => no header
   * @param label
   * @return one string or null
   */
  public static String askInfo(String title, String header, String label) {
    TextInputDialog dia = new TextInputDialog();

    dia.setTitle(title);
    dia.setHeaderText(header);
    dia.setContentText(label);

    Optional<String> text = dia.showAndWait();

    return text.isPresent() ? text.get() : null;
  }

}
