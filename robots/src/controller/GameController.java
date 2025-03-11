package controller;

import model.Model;
import view.GameVisualizer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private final Model model;
    private final GameVisualizer gameVisualizer;

    public GameController() {
        model = new Model(); // Model теперь реализует EntityProvider
        gameVisualizer = new GameVisualizer(model); // Передаем Model как EntityProvider

        Timer timer = new Timer(10, e -> {
            model.update(10);
            gameVisualizer.repaint();
        });
        timer.start();

        gameVisualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.sendMouseClickEvent(e.getX(), e.getY());
            }
        });
    }

    public void start() {
        JFrame frame = new JFrame("Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameVisualizer);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}