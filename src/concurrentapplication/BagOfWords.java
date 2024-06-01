/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package concurrentapplication;

/**
 *
 * @author JASON CODE
 */import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BagOfWords {

    private static class WordCountTask extends RecursiveTask<Map<String, Integer>> {
        private final String[] documents;
        private final int start;
        private final int end;

        public WordCountTask(String[] documents, int start, int end) {
            this.documents = documents;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Integer> compute() {
            if (end - start <= 1) {
                return countWords(documents[start]);
            } else {
                int mid = start + (end - start) / 2;
                WordCountTask leftTask = new WordCountTask(documents, start, mid);
                WordCountTask rightTask = new WordCountTask(documents, mid, end);
                leftTask.fork();
                rightTask.fork();
                Map<String, Integer> rightResult = rightTask.join();
                Map<String, Integer> leftResult = leftTask.join();

                // Print the number of threads in the pool
                //System.out.println("Number of threads in the pool: " + ForkJoinPool.commonPool().getParallelism());

                return mergeWordCounts(leftResult, rightResult);
            }
        }

        private Map<String, Integer> countWords(String document) {
            String[] words = document.split("\\s+"); // Split the document into words
            Map<String, Integer> wordCount = new ConcurrentHashMap<>();

            for (String word : words) {
                wordCount.merge(word, 1, Integer::sum); // Increment word count
            }

            return wordCount;
        }

        private Map<String, Integer> mergeWordCounts(Map<String, Integer> leftCounts, Map<String, Integer> rightCounts) {
            // Merge word counts from left and right tasks
            Map<String, Integer> mergedCounts = new ConcurrentHashMap<>(leftCounts); // Initialize with leftCounts

            // Merge rightCounts into mergedCounts
            rightCounts.forEach((word, count) -> mergedCounts.merge(word, count, Integer::sum));

            return sortWordCounts(mergedCounts); // Sort word counts before returning
        }

        private Map<String, Integer> sortWordCounts(Map<String, Integer> wordCounts) {
            // Sort word counts based on the number of occurrences
            return wordCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
        }
    }

    public Map<String, Integer> calculateBagOfWords(String[] documents) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        return forkJoinPool.invoke(new WordCountTask(documents, 0, documents.length));
    }
}
