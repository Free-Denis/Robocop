package model.entities;

import controller.ModelController;
import model.GameMath;
import model.GameState;

import java.util.Optional;

public class LosePoint implements Entity {
    private final int x;
    private final int y;
    private final int size = 100;

    public LosePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void update(ModelController controller, double duration) {
        if (controller.checkGameState() == GameState.PLAYING) {
            Optional<Robot> robotOpt = controller.findFirst(e -> e instanceof Robot)
                    .map(e -> (Robot) e);

            if (robotOpt.isPresent()) {
                Robot robot = robotOpt.get();
                double distance = GameMath.distance(x, y, robot.getPositionX(), robot.getPositionY());
                if (distance < size / 2.0) {
                    controller.setGameState(GameState.DEFEAT);
                }
            }
        }
    }
}