package model;

import controller.ModelController;
import model.entities.*;

import java.util.*;
import java.util.function.Predicate;

public class Model implements EntityProvider {
    private final List<Entity> entities = new ArrayList<>();
    private GameState currentGameState = GameState.PLAYING;

    public Model() {
        initializeLevel();
    }

    private void initializeLevel() {
        entities.clear();

        // Create and add entities
        Robot robot = new Robot();
        robot.setPosition(100, 100);
        robot.setTargetPosition(150, 100);
        entities.add(robot);

        entities.add(new WinPoint(500, 500));
        entities.add(new LosePoint(700, 700));
    }

    public void reset() {
        currentGameState = GameState.PLAYING;
        initializeLevel();
        findFirst(e -> e instanceof Robot).ifPresent(e -> {
            Robot r = (Robot) e;
            r.setPosition(100, 100);
            r.setTargetPosition(150, 100);
        });
    }

    public void InternalUpdate(ModelController controller, double duration) {
        List<Entity> currentEntities = new ArrayList<>(entities);
        for (Entity entity : currentEntities) {
            if (currentGameState == GameState.PLAYING) {
                entity.update(controller, duration);
            }
        }
    }

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Optional<T> findFirst(Predicate<Entity> predicate) {
        return (Optional<T>) entities.stream()
                .filter(predicate)
                .findFirst();
    }

    @Override
    public GameState checkGameState() {
        return currentGameState;
    }

    public void setGameState(GameState newState) {
        if (this.currentGameState == GameState.PLAYING) {
            this.currentGameState = newState;
            System.out.println("Game State Changed To: " + newState); // For debugging
        }
    }
}
