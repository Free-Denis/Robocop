
package view;

import model.entities.Obstacle;
import java.awt.*;

public class ObstacleVisualizer extends EntityVisualizer<Obstacle> {
    @Override
    public void draw(Graphics2D g, Obstacle obstacle) {
        g.setColor(Color.BLACK);
        g.fillRect(obstacle.getX(), obstacle.getY(),
                obstacle.getWidth(), obstacle.getHeight());
    }
}