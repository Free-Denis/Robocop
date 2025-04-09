package model.entities;

import model.GameState;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface EntityProvider {
    List<Entity> getEntities();
    <T extends Entity> Optional<T> findFirst(Predicate<Entity> predicate);
    GameState checkGameState();
}