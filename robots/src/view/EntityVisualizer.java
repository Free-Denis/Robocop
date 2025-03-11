package view;

import model.Entity;
import java.awt.Graphics2D;

public abstract class EntityVisualizer<E extends Entity> {
    public abstract void draw(Graphics2D g, E entity);
}