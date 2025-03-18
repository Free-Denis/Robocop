package model;

import java.util.ArrayList;
import java.util.List;

public class Model implements EntityProvider {
    private final List<Entity> entities = new ArrayList<>();
    private final Robot robot;

    public Model() {
        robot = new Robot();
        entities.add(robot);
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    public Robot getRobot() {
        return robot;
    }

    public void sendMouseClickEvent(int x, int y) {
        robot.setTargetPosition(x, y);
    }

    public void update(double duration) {
        for (Entity entity : entities) {
            entity.update(duration);
        }
    }
}