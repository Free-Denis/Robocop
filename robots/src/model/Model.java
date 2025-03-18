package model;

import java.util.ArrayList;
import java.util.List;

public class Model implements EntityProvider {
    private final List<Entity> entities = new ArrayList<>();
    private final Robot robot;
    private final WinPoint winPoint;
    private final LosePoint losePoint;

    public Model() {
        robot = new Robot();
        winPoint = new WinPoint(500, 500);
        losePoint = new LosePoint(700, 700);

        entities.add(robot);
        entities.add(winPoint);
        entities.add(losePoint);
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    public Robot getRobot() {
        return robot;
    }

    public WinPoint getWinPoint() {
        return winPoint;
    }

    public LosePoint getLosePoint() {
        return losePoint;
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