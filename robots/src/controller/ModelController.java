package controller;

import model.*;
import model.entities.Entity;
import model.entities.Robot;

import java.util.Optional;
import java.util.function.Predicate;

public class ModelController implements Controller {
    private final Model model;
    private double updateDuration;

    public ModelController(Model model) {
        this.model = model;
        this.updateDuration = 10;
    }

    @Override
    public void update() {

        model.InternalUpdate(this, updateDuration);
    }

    @Override
    public void setUpdateDuration(double duration) {

        this.updateDuration = duration;
    }

    @Override
    public void setRobotTargetPosition(int x, int y) {
        model.findFirst(e -> e instanceof Robot)
                .ifPresent(robot -> ((Robot) robot).setTargetPosition(x, y));
    }

    @Override
    public GameState checkGameState() {
        return model.checkGameState();
    }

    @Override
    public void resetGame() {
        model.reset();
    }

    @Override
    public <T extends Entity> Optional<T> findFirst(Predicate<Entity> predicate) {
        return model.findFirst(predicate);
    }

    @Override
    public void setGameState(GameState state) {
        model.setGameState(state);
    }
}