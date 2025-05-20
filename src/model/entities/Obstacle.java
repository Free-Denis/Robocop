// src/model/entities/Obstacle.java
package model.entities;

import model.ModelController;
import java.awt.*;

public class Obstacle implements Entity {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public void update(ModelController controller, double duration) {

    }
}