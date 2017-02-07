package app.ihms;

import app.beans.Module;
import app.workers.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author PA
 */
public class KeywordsController implements Initializable,  TabController{

    private ConfigWorker wrk;

    @FXML
    private TableView<Module> tableConf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        }
    
    @Override
    public void init(ConfigWorker wrk){
        this.wrk = wrk;
        tableConf.getItems().clear();
        tableConf.getColumns().clear();
        tableConf.setItems(wrk.getModules());
        TableColumn<Module, String> colName = new TableColumn<>("Module");
        colName.setPrefWidth(75);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableConf.getColumns().add(colName);

        for (String keyword : wrk.getKeywords()) {
            TableColumn<Module, Boolean> colKeyword = new TableColumn<>(keyword);
            colKeyword.setPrefWidth(75);
            colKeyword.setCellValueFactory(cellData -> cellData.getValue().getKeywords().get(keyword));
            colKeyword.setCellFactory(chk -> new CheckBoxTableCell<>());
            tableConf.getColumns().add(colKeyword);
        }
    }
}
