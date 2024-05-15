/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package concurrentapplication;

import java.util.ArrayList;

/**
 *
 * @author Ilham
 */
public class Algorithm {
    private String name;
    private double performance;
    private ArrayList<Word> mostFrequentWords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public ArrayList<Word> getMostFrequentWords() {
        return mostFrequentWords;
    }

    public void setMostFrequentWords(ArrayList<Word> mostFrequentWords) {
        this.mostFrequentWords = mostFrequentWords;
    }
    
    
}
