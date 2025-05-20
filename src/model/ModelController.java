package model;

import model.entities.Entity;

import java.util.Optional;
import java.util.function.Predicate;

public class ModelController {
    private final Model model;

    public ModelController(Model model) {
        this.model = model;
    }


    public <T extends Entity> Optional<T> findFirst(Predicate<Entity> predicate) {
        return model.findFirst(predicate);
    }

    public void setGameState(GameState state) {
        model.setGameState(state);
    }

}