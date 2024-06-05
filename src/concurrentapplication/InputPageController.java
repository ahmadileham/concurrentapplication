package concurrentapplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
        submitFileBtn.setDisable(true);
    }

    @FXML
    private void submitFile(ActionEvent event) {
        try {
            // Read and clean the text file once
            String cleanedText = readAndCleanText(file);

            // Execute each algorithm
            qayleefAlgorithm(cleanedText);
            jsAlgorithm(cleanedText);
            jasonAlgorithm(cleanedText);

            root = FXMLLoader.load(getClass().getResource("/concurrentapplication/resultPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void browseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            submitFileBtn.setDisable(false);
            this.file = selectedFile;
            filePath = selectedFile.getAbsolutePath();
        }
    }

    private String readAndCleanText(File file) throws IOException {
        StringBuilder cleanedText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Retain apostrophes and dashes within words, remove other punctuation
                line = line.replaceAll("[^a-zA-Z0-9\\s'-]", "").toLowerCase();
                cleanedText.append(line).append(" ");
            }
        }
        return cleanedText.toString();
    }

    private void qayleefAlgorithm(String cleanedText) {
        Map<String, Integer> globalWordCounts = new ConcurrentHashMap<>();
        String[] tokens = cleanedText.split("\\s+");
        int batchSize = 1000; 

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        long startTime = System.nanoTime();
        for (int i = 0; i < tokens.length; i += batchSize) {
            int end = Math.min(i + batchSize, tokens.length);
            String[] batch = Arrays.copyOfRange(tokens, i, end);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Map<String, Integer> localCounts = new HashMap<>();
                for (String token : batch) {
                    localCounts.merge(token.toLowerCase(), 1, Integer::sum);
                }
                localCounts.forEach((key, value) -> globalWordCounts.merge(key, value, Integer::sum));
            });

            futures.add(future);
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.nanoTime();
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        // qayleefAlgo.setTime(processingTimeMs); 
        time2 = String.valueOf(processingTimeMs);

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(globalWordCounts.entrySet());
        entryList.sort((e1, e2) -> {
            int compare = e2.getValue().compareTo(e1.getValue());
            if (compare == 0) {
                return e1.getKey().compareTo(e2.getKey());
            }
            return compare;
        });

        for (Map.Entry<String, Integer> entry : entryList) {
            wordList2.add(new Word(entry.getKey(), entry.getValue()));
        }
    }

    private void jsAlgorithm(String cleanedText) {
        Map<String, Integer> tokenFreq = new HashMap<>();
        String[] tokens = cleanedText.split("\\s+");

        long startTime = System.nanoTime();
        for (String token : tokens) {
            tokenFreq.put(token, tokenFreq.getOrDefault(token, 0) + 1);
        }
        long endTime = System.nanoTime();
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        jsAlgo.setTime(processingTimeMs);
        time1 = String.valueOf(processingTimeMs);

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tokenFreq.entrySet());
        sortedEntries.sort((entry1, entry2) -> {
            int freqCompare = entry2.getValue().compareTo(entry1.getValue());
            return freqCompare != 0 ? freqCompare : entry1.getKey().compareTo(entry2.getKey());
        });

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            wordList1.add(new Word(entry.getKey(), entry.getValue()));
        }
    }

    private void jasonAlgorithm(String cleanedText) throws IOException {
        String[] documents = cleanedText.split("\\s+");

        long startTime = System.nanoTime();
        BagOfWords bagOfWords = new BagOfWords();
        Map<String, Integer> result = bagOfWords.calculateBagOfWords(documents);
        long endTime = System.nanoTime();
        long processingTimeMs = (endTime - startTime) / 1_000_000;
        jasonAlgo.setTime(processingTimeMs);
        time3 = String.valueOf(processingTimeMs);

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            wordList3.add(new Word(entry.getKey(), entry.getValue()));
        }
    }
}
