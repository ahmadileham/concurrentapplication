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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
/**
 * FXML Controller class
 *
 * @author Ilham
 */
public class InputPageController implements Initializable {
    
    static Algorithm qayleefAlgo = new Algorithm("Qayleef");
    static Algorithm jasonAlgo = new Algorithm("Jason");
    static Algorithm jsAlgo = new Algorithm("Jing Shan");
    
    File file;
    static String filePath;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    

    @FXML
    private TextField filePathField;
    
    @FXML
    private Button submitFileBtn;
    
    static ArrayList<Word> wordList1 = new ArrayList<>();
    static ArrayList<Word> wordList2 = new ArrayList<>();
    static ArrayList<Word> wordList3 = new ArrayList<>();
    
    static String time1;
    static String time2;
    static String time3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void submitFile(ActionEvent event) throws IOException {
        
        qayleefAlgorithm();
        jsAlgorithm();
        jasonAlgorithm();
            
        root = FXMLLoader.load(getClass().getResource("/concurrentapplication/resultPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    @FXML
    private void browseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            submitFileBtn.setDisable(false);
            this.file = selectedFile;
            this.filePath = selectedFile.getAbsolutePath();
        }
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

    private void qayleefAlgorithm() {
        
        long startTime = System.nanoTime();
        String[] texts = readTextFromFile(file);
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
            wordList2.add(new Word(entry.getKey(), entry.getValue()));
        }
        long endTime = System.nanoTime();
        
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        qayleefAlgo.setTime(processingTimeMs);
        time2 = String.valueOf(processingTimeMs);
    }
    
    private void jsAlgorithm(){
        long startTime = System.nanoTime();
        Map<String, Integer> tokenFreq = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                String[] tokens = line.split("\\s+");
                for(String token : tokens){
                    tokenFreq.put(token, tokenFreq.getOrDefault(token, 0) + 1);
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        // Convert tokenFreq map entries into Word objects and add them to wordList2
        for (Map.Entry<String, Integer> entry : tokenFreq.entrySet()) {
            wordList1.add(new Word(entry.getKey(), entry.getValue()));
        }
        long endTime = System.nanoTime();
        
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        jsAlgo.setTime(processingTimeMs);
        time1 = String.valueOf(processingTimeMs);
    }
    
    private void jasonAlgorithm() throws IOException{
        long startTime = System.nanoTime();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        // Treat each line as a separate document
        String[] documents = lines.toArray(new String[0]);
        
        // Calculate the bag of words
        BagOfWords bagOfWords = new BagOfWords();
        Map<String, Integer> result = bagOfWords.calculateBagOfWords(documents);
        
        // Convert the result map entries into Word objects and add them to wordList3
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            wordList3.add(new Word(entry.getKey(), entry.getValue()));
        }

        long endTime = System.nanoTime();
        
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        jasonAlgo.setTime(processingTimeMs);
        time3 = String.valueOf(processingTimeMs);
    }
    
}
