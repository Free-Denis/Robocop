package model.entities;

import controller.ModelController;
import model.GameMath;
import model.GameState;

import java.util.Optional;

public class WinPoint implements Entity {
    private final int x;
    private final int y;
    private final int size = 100;

    public WinPoint(int x, int y) {
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
            // Find the robot using the controller
            Optional<Robot> robotOpt = controller.findFirst(e -> e instanceof Robot)
                    .map(e -> (Robot) e);

            if (robotOpt.isPresent()) {
                Robot robot = robotOpt.get();
                double distance = GameMath.distance(x, y, robot.getPositionX(), robot.getPositionY());
                // Check if the robot center is within half the size of this point
                if (distance < size / 2.0) {
                    // Signal win condition to the controller
                    controller.setGameState(GameState.VICTORY);
                }
            }
        }
    }
}