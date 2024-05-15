/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package concurrentapplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
/**
 * FXML Controller class
 *
 * @author Ilham
 */
public class InputPageController implements Initializable {

    @FXML
    private TextField filePathField;
    
    static ArrayList<Word> wordList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void browseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            String[] texts = readTextFromFile(selectedFile);
            
            ExecutorService executor = Executors.newFixedThreadPool(3);
            Map<String, Integer> globalWordCounts = new ConcurrentHashMap<>();
            for (String text : texts) {
                executor.submit(() -> processText(text, globalWordCounts));
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                // Wait
            }
            for (Map.Entry<String, Integer> entry : globalWordCounts.entrySet()) {
                wordList.add(new Word(entry.getKey(), entry.getValue()));
            }
            // Load the result page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/concurrentapplication/resultPage.fxml"));
            Parent root = loader.load();
            ResultPageController resultController = loader.getController();

            // Show the result page
            Stage resultStage = new Stage();
            resultStage.setScene(new Scene(root));
            resultStage.show();
        }
    } 
    
    private void printFileContent(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("Content of the file:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private String calculateWordFrequencies(File file) {
        // Implement your logic to calculate word frequencies here
        // Return the word frequencies as a String
        // For example:
        return "Word1: 10\nWord2: 5\nWord3: 8";
    }
    
    static void processText(String text, Map<String, Integer> wordCounts) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
    }
    
    static String[] readTextFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString().split("\\n");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];         }
    }
    
}
