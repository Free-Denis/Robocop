package controller;

import model.GameState;
import model.Model;
import view.GameVisualizer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private final GameVisualizer gameVisualizer;
    private Timer timer;
    private final ModelController modelController;
    private final Model model;

    public GameController() {
        model = new Model();
        modelController = new ModelController(model);
        gameVisualizer = new GameVisualizer(model);

        modelController.setUpdateDuration(10);

        timer = new Timer(10, e -> {
            modelController.update();
            gameVisualizer.repaint();

            GameState state = modelController.checkGameState();

            if (state != GameState.PLAYING) {
                timer.stop();
                String message = (state == GameState.VICTORY)
                        ? "Победа! Вы достигли точки победы!"
                        : "Поражение! Вы достигли точки поражения!";
                endGame(message);
            }
        });
        timer.start();

        gameVisualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modelController.checkGameState() == GameState.PLAYING) {
                    modelController.setRobotTargetPosition(e.getX(), e.getY());
                }
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
        modelController.resetGame();
        gameVisualizer.repaint();
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