package model.entities;

import controller.ModelController;

public interface Entity {
    void update(ModelController controller, double duration);
}