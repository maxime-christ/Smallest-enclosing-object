package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

import model.Algorithm;
import model.MiniDiscAlgorithm;
import model.NaiveCircleAlgorithm;
import model.Vector2;

import model.RotatingCaliperAlgorithm;

import view.Window;

public class Controller extends Observable{
    private Window window;
    private static int NUMBER_OF_POINTS = 20;
    private Vector2[] pointSet;
    private boolean computing = false;
    private Algorithm algo;
    
    public Controller() {
        super();
        window = new Window(this);
        window.setVisible(true);
        addObserver(window);
        createNaiveCircle();
    }
    
    public void createRandomSet() {
        if(!computing) {
            // Random points generation
            pointSet = new Vector2[NUMBER_OF_POINTS];
            Random randGenerator = new Random();
            for(int i = 0; i < NUMBER_OF_POINTS; i++) {
                double x = 20 * (randGenerator.nextDouble() - 0.5);
                double y = 20 * (randGenerator.nextDouble() - 0.5);
                pointSet[i] = new Vector2(x, y);
            }
            // Sort those points
            //mergeSort(pointSet);
            
            if(algo != null)
                algo.setInputs(pointSet);
        }
    }
    
    public void loadSetFromFile(String filename) {
        if(!computing) {
            try {
                // Read file
                FileReader fileReader = new FileReader(filename);
                BufferedReader textReader = new BufferedReader(fileReader);
                String line;
                LinkedList<String[]> coordinatesList = new LinkedList<String[]>();
                while((line = textReader.readLine()) != null) {
                    String[] coordinates = line.split(" ");
                    if(coordinates.length != 2){
                        System.out.println("Wrong file format");
                        return;
                    }
                    coordinatesList.push(coordinates);
                }
                
                // Create the point set
                pointSet = new Vector2[coordinatesList.size()];
                for(int i = 0; i < pointSet.length; i++) {
                    pointSet[i] = new Vector2(Double.parseDouble(coordinatesList.get(i)[0]), Double.parseDouble(coordinatesList.get(i)[1]));
                }
                
                //mergeSort(pointSet);
                
                if(algo != null)
                    algo.setInputs(pointSet);
            } catch (FileNotFoundException e) {
                // Shouldn't happen
            } catch (IOException e) {
                System.out.println("Wrong file format");
            }
        }
    }
    
    public void createNaiveCircle() {
        algo = new NaiveCircleAlgorithm();
        setChanged();
        notifyObservers(algo);
        if(pointSet != null) {
            algo.setInputs(pointSet);
        }
    }
    
    public void createMiniDisc() {
        algo = new MiniDiscAlgorithm();
        setChanged();
        notifyObservers(algo);
        if(pointSet != null) {
            algo.setInputs(pointSet);
        }
    }
    
    public void createRotatingCaliper() {
        algo = new RotatingCaliperAlgorithm();
        setChanged();
        notifyObservers(algo);
        if(pointSet != null) {
            algo.setInputs(pointSet);
        }
    }
    
    public double executeAlgo() {
        double initTime = System.currentTimeMillis();
        if(algo != null && pointSet != null)
            algo.execute();
        return System.currentTimeMillis() - initTime;
    }
    
    public void findMaxSetSize() {
        int increment = 0;
        if(algo.getClass() == RotatingCaliperAlgorithm.class)
            return;
        else if (algo.getClass() == NaiveCircleAlgorithm.class) {
            NUMBER_OF_POINTS = 45;
            increment = 5;
        } else if (algo.getClass() == MiniDiscAlgorithm.class) {
            NUMBER_OF_POINTS = 60000;
            increment = 1000;
        }
    
        double exeTime;
        
        do {
            NUMBER_OF_POINTS+=increment;
            createRandomSet();
        } while ((exeTime = executeAlgo()) < 1000);
        System.out.println("for " + NUMBER_OF_POINTS + " points, the execution time is " + executeAlgo());
        System.out.println("Last time it took " + exeTime);

        NUMBER_OF_POINTS = 20;
    }
    
    public static void main(String[] args) {
        Controller c = new Controller();
    }
}
