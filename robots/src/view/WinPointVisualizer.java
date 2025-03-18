package view;

import model.WinPoint;
import java.awt.*;

public class WinPointVisualizer extends EntityVisualizer<WinPoint> {
    @Override
    public void draw(Graphics2D g, WinPoint winPoint) {
        g.setColor(Color.BLUE);
        g.fillOval(winPoint.getX() - winPoint.getSize() / 2, winPoint.getY() - winPoint.getSize() / 2, winPoint.getSize(), winPoint.getSize());
        g.setColor(Color.BLACK);
        g.drawString("Win", winPoint.getX() + winPoint.getSize() / 2 + 5, winPoint.getY());
    }
}