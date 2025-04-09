package controller;

import model.GameState;
import model.entities.Entity;
import model.entities.Robot;

import java.util.Optional;
import java.util.function.Predicate;

public interface Controller {
    void update();
    void setRobotTargetPosition(int x, int y);
    GameState checkGameState();
    <T extends Entity> Optional<T> findFirst(Predicate<Entity> predicate);
    void setGameState(GameState state);
    void setUpdateDuration(double duration);
    void resetGame();
}