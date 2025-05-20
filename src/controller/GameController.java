package controller;

import model.GameState;
import model.Model;
import view.GameVisualizer;
import view.TimerView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private final GameVisualizer gameVisualizer;
    private Timer timer;
    private final Model model;
    private TimerView timerView;
    private Timer uiTimer;

    public GameController() {
        model = new Model();
        gameVisualizer = new GameVisualizer(model);
        timerView = new TimerView();

        model.setUpdateDuration(10);

        timer = new Timer(10, e -> {
            model.update(model.getUpdateDuration());
            gameVisualizer.repaint();

            GameState state = model.checkGameState();

            if (state != GameState.PLAYING) {
                timer.stop();
                uiTimer.stop();
                String message = (state == GameState.VICTORY)
                        ? "Победа! Вы достигли точки победы!"
                        : "Поражение! Время вышло!";
                endGame(message);
            }
        });

        uiTimer = new Timer(100, e -> {
            timerView.updateTime(model.getTimerModel().getRemainingTimeMillis());
        });

        gameVisualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (model.checkGameState() == GameState.PLAYING) {
                    model.setRobotTargetPosition(e.getX(), e.getY());
                }
            }
        });
    }

    private void endGame(String message) {
        timer.stop();
        uiTimer.stop();

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
            timerView.dispose();
            System.exit(0);
        }
    }

    private void restartGame() {

        timer.stop();
        uiTimer.stop();
        timerView.dispose();


        timerView = new TimerView();
        timerView.setVisible(true);


        model.resetGame();
        gameVisualizer.repaint();


        timer.start();
        uiTimer.start();
        model.getTimerModel().start();
    }

    public void start() {
        JFrame frame = new JFrame("Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameVisualizer);
        frame.setSize(800, 600);
        frame.setVisible(true);

        timerView.setVisible(true);
        timer.start();
        uiTimer.start();
        model.getTimerModel().start();
    }
}