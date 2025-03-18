package controller;

import model.Model;
import view.GameVisualizer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private final Model model;
    private final GameVisualizer gameVisualizer;
    private Timer timer;

    public GameController() {
        model = new Model();
        gameVisualizer = new GameVisualizer(model);

        timer = new Timer(10, e -> {
            model.update(10);
            gameVisualizer.repaint();

            if (model.getWinPoint().checkWin(model.getRobot())) {
                endGame("Победа! Вы достигли точки победы!");
            } else if (model.getLosePoint().checkLose(model.getRobot())) {
                endGame("Поражение! Вы достигли точки поражения!");
            }
        });
        timer.start();

        gameVisualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.sendMouseClickEvent(e.getX(), e.getY());
            }
        });
    }

    private void endGame(String message) {
        timer.stop();

        int option = JOptionPane.showOptionDialog(
                gameVisualizer,
                message,
                "Игра завершена",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Перезапустить", "Выход"},
                "Перезапустить"
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        model.getRobot().setPosition(100, 100);
        model.getRobot().setTargetPosition(150, 100);
        timer.start();
    }

    public void start() {
        JFrame frame = new JFrame("Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameVisualizer);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}