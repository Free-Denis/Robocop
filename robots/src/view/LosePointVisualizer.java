package view;

import model.LosePoint;
import java.awt.*;

public class LosePointVisualizer extends EntityVisualizer<LosePoint> {
    @Override
    public void draw(Graphics2D g, LosePoint losePoint) {
        g.setColor(Color.RED);
        g.fillOval(losePoint.getX() - losePoint.getSize() / 2, losePoint.getY() - losePoint.getSize() / 2, losePoint.getSize(), losePoint.getSize());
        g.setColor(Color.BLACK);
        g.drawString("Lose", losePoint.getX() + losePoint.getSize() / 2 + 5, losePoint.getY());
    }
}