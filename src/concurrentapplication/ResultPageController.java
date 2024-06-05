package concurrentapplication;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class ResultPageController implements Initializable {

    @FXML
    private TableView<Word> tableView1;
    @FXML
    private Text time1;
    @FXML
    private TableView<Word> tableView2;
    @FXML
    private Text time2;
    @FXML
    private Text time3;
    @FXML
    private TableView<Word> tableView3;
    @FXML
    private Label os;
    @FXML
    private Label textsize;

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    long fileSize = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate TableView for Algorithm 1
        setWordList(tableView1, InputPageController.wordList1);

        // Populate TableView for Algorithm 2
        setWordList(tableView2, InputPageController.wordList2);

        // Populate TableView for Algorithm 3
        setWordList(tableView3, InputPageController.wordList3);

        // Time taken for every algorithm
        time1.setText("Time taken: " + InputPageController.time1 + "ms");
        time2.setText("Time taken: " + InputPageController.time2 + "ms");
        time3.setText("Time taken: " + InputPageController.time3 + "ms");

        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        os.setText("Operating System Name: " + osName + "\nOperating System Version: " + osVersion + "\nOperating System Architecture: " + osArch);

        try {
            fileSize = Files.size(Paths.get(InputPageController.filePath));
            textsize.setText("File size: " + fileSize + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWordList(TableView<Word> tableView, ArrayList<Word> wordList) {
        // Clear existing data
        tableView.getItems().clear();

        // Create observable list for table data
        ObservableList<Word> data = FXCollections.observableArrayList(wordList);

        // Set cell value factories for table columns
        TableColumn<Word, String> wordColumn = new TableColumn<>("Word");
        wordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWord()));

        TableColumn<Word, Integer> frequencyColumn = new TableColumn<>("Frequency");
        frequencyColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFrequency()).asObject());

        tableView.getColumns().clear();
        tableView.getColumns().addAll(wordColumn, frequencyColumn);

        // Set table data
        tableView.setItems(data);
    }

    @FXML
    private void resetPage(ActionEvent event) throws IOException {
        // Reset any static data if needed
        InputPageController.wordList1.clear();
        InputPageController.wordList2.clear();
        InputPageController.wordList3.clear();
        InputPageController.jsAlgo.reset();
        InputPageController.qayleefAlgo.reset();
        InputPageController.jasonAlgo.reset();
        InputPageController.time1 = "";
        InputPageController.time2 = "";
        InputPageController.time3 = "";
        fileSize = 0;

        // Load Input Page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/concurrentapplication/InputPage.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
