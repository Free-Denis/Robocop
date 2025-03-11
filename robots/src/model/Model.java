package model;

import java.util.ArrayList;
import java.util.List;

public class Model implements EntityProvider { // Реализуем интерфейс EntityProvider
    private final List<Entity> entities = new ArrayList<>();

    public Model() {
        entities.add(new Robot()); // Добавляем робота в модель
    }

    @Override
    public List<Entity> getEntities() { // Реализуем метод интерфейса
        return entities;
    }

    public void sendMouseClickEvent(int x, int y) {
        for (Entity entity : entities) {
            if (entity instanceof Robot) {
                ((Robot) entity).setTargetPosition(x, y);
            }
        }
    }

    public void update(double duration) {
        for (Entity entity : entities) {
            entity.update(duration);
        }
    }
}