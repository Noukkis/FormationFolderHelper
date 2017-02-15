package app.ihms;

import app.beans.Module;
import app.beans.OtherWord;
import app.workers.config.ConfigWorker;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;



/**
 *
 * @author PA
 */
public class KeywordsCtrl implements Initializable, TabCtrl {

    private ConfigWorker wrk;

    @FXML
    private TableView<Module> tableConf;
    @FXML
    private ListView<OtherWord> lstOthers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void init(ConfigWorker wrk) {
        this.wrk = wrk;
        tableConf.getItems().clear();
        tableConf.getColumns().clear();
        tableConf.setItems(wrk.getModules());
        TableColumn<Module, String> colName = new TableColumn<>("Module");
        colName.setPrefWidth(60);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableConf.getColumns().add(colName);

        for (String keyword : wrk.getKeywords()) {
            TableColumn<Module, Boolean> colKeyword = new TableColumn<>(keyword);
            colKeyword.setPrefWidth(75);
            colKeyword.setCellValueFactory(cellData -> cellData.getValue().getKeywords().get(keyword));
            colKeyword.setCellFactory(chk -> new CheckBoxTableCell<>());
            tableConf.getColumns().add(colKeyword);
        }
         lstOthers.itemsProperty().bind(new SimpleListProperty<>(wrk.getOthersWords()));
        lstOthers.setCellFactory(CheckBoxListCell.forListView((OtherWord item) -> {
             return item.getActive();          
        }));
    }
}
