/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package concurrentapplication;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author umiar
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Populate TableView for Algorithm 1
        setWordList(tableView1, InputPageController.wordList1);

        // Populate TableView for Algorithm 2
        setWordList(tableView2, InputPageController.wordList2);

        // Populate TableView for Algorithm 3
        setWordList(tableView3, InputPageController.wordList3);
        
        //Time taken for every algorithm
        time1.setText("Time taken: " + InputPageController.qayleefAlgo.getTime() + "ms");
        time2.setText("Time taken: " + InputPageController.jsAlgo.getTime() + "ms");
        time3.setText("Time taken: " + InputPageController.jasonAlgo.getTime() + "ms");
        
        
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        os.setText("Operating System Name: " + osName + "\nOperating System Version: " + osVersion + "\nOperating System Architecture: " + osArch);
        
        Path filePath = Paths.get(InputPageController.filePath);
        try {
            long fileSize = Files.size(filePath);
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
    wordColumn.setCellValueFactory(cellData -> {
        // Here we directly access the word field of the Word object
        return new SimpleStringProperty(cellData.getValue().getWord());
    });

    TableColumn<Word, Integer> frequencyColumn = new TableColumn<>("Frequency");
    frequencyColumn.setCellValueFactory(cellData -> {
        // Here we directly access the frequency field of the Word object
        return new SimpleIntegerProperty(cellData.getValue().getFrequency()).asObject();
    });

    tableView.getColumns().clear();
    tableView.getColumns().addAll(wordColumn, frequencyColumn);

    // Set table data
    tableView.setItems(data);
}    
    
}
