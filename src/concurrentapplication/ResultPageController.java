/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package concurrentapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ilham
 */
public class ResultPageController implements Initializable {
    @FXML
    private TextArea wordListText;
    
    private String wordsDisplay="";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWordList(InputPageController.wordList);
    }

    public void setWordList(ArrayList<Word> wordList) {
        StringBuilder sb = new StringBuilder();
        for (Word word : wordList) {
            sb.append(word).append("\n");
        }
        for(int i=0;i<InputPageController.wordList.size();i++){
            sb.append(InputPageController.wordList.get(i).toString()).append("\n");
        }
        System.out.println(sb);
        wordListText.setText(sb.toString());
    }
    
}
