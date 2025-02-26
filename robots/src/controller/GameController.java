package controller;

import model.RobotModel;
import view.GameVisualizer;

import javax.swing.*;

public class GameController {
    private final RobotModel robotModel;
    private final GameVisualizer gameVisualizer;

    public GameController() {
        robotModel = new RobotModel();
        gameVisualizer = new GameVisualizer(robotModel);
    }

    public void start() {
        JFrame frame = new JFrame("Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameVisualizer);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}