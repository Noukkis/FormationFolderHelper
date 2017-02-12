package app.ihms;

import app.workers.config.ConfigWorker;
import app.beans.Module;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.MapChangeListener;
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
public class KeywordsCtrl implements Initializable,  TabCtrl{

    private ConfigWorker wrk;

    @FXML
    private TableView<Module> tableConf;
    @FXML
    private ListView<Entry<String, BooleanProperty>> lstOthers;

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
        lstOthers.getItems().setAll(wrk.getOthersWords());
        lstOthers.setCellFactory(CheckBoxListCell.forListView((String item) -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.bindBidirectional(wrk.getOthersWords().get(item));
            return observable ;
        }));
        
        wrk.getOthersWords().b;
    }
}
