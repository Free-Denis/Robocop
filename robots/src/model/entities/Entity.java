package model.entities;

import model.ModelController;

public interface Entity {
    void update(ModelController controller, double duration);
}