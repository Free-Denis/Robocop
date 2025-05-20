// src/view/TimerView.java
package view;

import javax.swing.*;
import java.awt.*;

public class TimerView extends JFrame {
    private final JLabel timeLabel;

    public TimerView() {
        setTitle("Game Timer");
        setSize(200, 100);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        timeLabel = new JLabel("02:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(timeLabel);

        setLocation(100, 100); // Позиция окна
    }

    public void updateTime(long remainingMillis) {
        int minutes = (int) (remainingMillis / 60000);
        int seconds = (int) ((remainingMillis % 60000) / 1000);
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));

        if (remainingMillis < 10000) {
            timeLabel.setForeground(Color.RED);
        } else {
            timeLabel.setForeground(Color.BLACK);
        }
    }
}