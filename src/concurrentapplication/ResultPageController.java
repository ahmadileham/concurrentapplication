/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author umiar
 */
public class ResultPageController implements Initializable {

    @FXML
    private TextArea wordListText1;
    @FXML
    private Text time1;
    @FXML
    private TextArea wordListText2;
    @FXML
    private Text time2;
    @FXML
    private Text time3;
    @FXML
    private TextArea wordListText3;
    @FXML
    private Label os;
    @FXML
    private Label textsize;
    
    private String wordsDisplay1 = "";
    private String wordsDisplay2 = "";
    private String wordsDisplay3 = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setWordList1(InputPageController.wordList1, InputPageController.qayleefAlgo.getTime() + "");
        setWordList2(InputPageController.wordList2, InputPageController.jsAlgo.getTime() + "");
        setWordList3(InputPageController.wordList3, InputPageController.jasonAlgo.getTime() + "");
        
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
    
    public void setWordList1(ArrayList<Word> wordList, String time) {
        wordListText1.setText(formatWordList(wordList));
        time1.setText("Time taken: " + time + "ms");
    }
    
    public void setWordList2(ArrayList<Word> wordList, String time) {
        wordListText2.setText(formatWordList(wordList));
        time2.setText("Time taken: " + time + "ms");
    }
    
    public void setWordList3(ArrayList<Word> wordList, String time) {
        wordListText3.setText(formatWordList(wordList));
        time3.setText("Time taken: " + time + "ms");
    }

    private String formatWordList(ArrayList<Word> wordList) {
        // Determine the maximum word length
        int maxWordLength = 0;
        for (Word word : wordList) {
            maxWordLength = Math.max(maxWordLength, word.getWord().length());
        }

        // Add extra space between word and frequency
        int extraSpace = 5;
        int padding = maxWordLength + extraSpace;

        // Create the table header
        StringBuilder sb = new StringBuilder();
        String headerFormat = "%-" + padding + "s%s%n";
        sb.append(String.format(headerFormat, "Word", "Frequency"));
        sb.append("-".repeat(padding + 10)).append("\n");

        // Add the word list
        String rowFormat = "%-" + padding + "s%d%n";
        for (Word word : wordList) {
            sb.append(String.format(rowFormat, word.getWord(), word.getFrequency()));
        }

        return sb.toString();
    }
}
