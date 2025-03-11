package view;

import model.Entity;
import model.EntityProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameVisualizer extends JPanel {
    private final EntityProvider entityProvider;
    private final Map<Class<? extends Entity>, EntityVisualizer<?>> visualizers = new HashMap<>();

    public GameVisualizer(EntityProvider entityProvider) {
        this.entityProvider = entityProvider;
        // Регистрируем визуализатор для робота
        visualizers.put(model.Robot.class, new RobotVisualizer());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        List<Entity> entities = entityProvider.getEntities();
        for (Entity entity : entities) {
            EntityVisualizer<?> visualizer = visualizers.get(entity.getClass());
            if (visualizer != null) {
                // Используем cast для корректного типа
                drawEntity(g2d, entity, visualizer);
            }
        }
    }

    private <E extends Entity> void drawEntity(Graphics2D g, E entity, EntityVisualizer<?> visualizer) {
        @SuppressWarnings("unchecked")
        EntityVisualizer<E> typedVisualizer = (EntityVisualizer<E>) visualizer;
        typedVisualizer.draw(g, entity);
    }
}