package view;

import controller.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JRadioButton;

public class Window extends JFrame implements Observer{
    private Controller controller;
    
    private JPanel buttonPanel = new JPanel();
    private JButton randomButton = new JButton("Random points");
    private JButton readFromFileButton = new JButton("Points from file");
    private JButton execButton = new JButton("Execute");
    private JButton evaluateButton = new JButton("Evaluate");
    private DrawingPanel drawingPanel = new DrawingPanel();
    private JRadioButton naiveCircleButton = new JRadioButton("Naive circle");
    private JRadioButton miniDiscButton = new JRadioButton("Mini disc");
    private JRadioButton rotatingCaliperButton = new JRadioButton("Rotating Caliper");
    private ButtonGroup algorithm = new ButtonGroup();
    
    public Window(Controller aController) {
        //Basic window config
        super("Lab1: Computing convex hull");
        this.controller = aController;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1200, 700));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.createRandomSet();
            }
        });
        readFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(Window.this);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    controller.loadSetFromFile(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        execButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Thread thread = (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        controller.executeAlgo();
                    }
                }));
                thread.start();
            }
        });
        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Thread thread = (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        controller.findMaxSetSize();
                    }
                }));
                thread.start();
            }
        });
        naiveCircleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.createNaiveCircle();
            }
        });
        miniDiscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.createMiniDisc();
            }
        });
        rotatingCaliperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.createRotatingCaliper();
            }
        });

        algorithm.add(naiveCircleButton);
        algorithm.add(miniDiscButton);
        algorithm.add(rotatingCaliperButton);
        naiveCircleButton.setSelected(true);
        
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(randomButton);
        buttonPanel.add(readFromFileButton);
        buttonPanel.add(execButton);
        buttonPanel.add(evaluateButton);
        JPanel algoPanel = new JPanel();
        algoPanel.setLayout(new GridLayout(3,1));
        algoPanel.add(naiveCircleButton);
        algoPanel.add(miniDiscButton);
        algoPanel.add(rotatingCaliperButton);
        buttonPanel.add(algoPanel);

        this.add(buttonPanel, BorderLayout.WEST);
        
        //set drawing panel
        drawingPanel.setBackground(Color.WHITE);
        this.add(drawingPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable observable, Object object) {
        ((Observable)object).addObserver(drawingPanel);
    }
}
