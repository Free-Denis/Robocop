package view;

import model.entities.Robot;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class RobotVisualizer extends EntityVisualizer<Robot> {
    @Override
    public void draw(Graphics2D g, Robot robot) {
        AffineTransform originalTransform = g.getTransform();

        AffineTransform robotTransform = AffineTransform.getRotateInstance(robot.getDirection(), robot.getPositionX(), robot.getPositionY());
        g.setTransform(robotTransform);
        g.setColor(Color.MAGENTA);
        fillOval(g, (int) robot.getPositionX(), (int) robot.getPositionY(), 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, (int) robot.getPositionX(), (int) robot.getPositionY(), 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, (int) robot.getPositionX() + 10, (int) robot.getPositionY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, (int) robot.getPositionX() + 10, (int) robot.getPositionY(), 5, 5);

        g.setTransform(originalTransform);

        Point finalTarget = robot.getFinalTarget();
        if (finalTarget != null) {
            drawTarget(g, finalTarget.x, finalTarget.y);
        }
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}