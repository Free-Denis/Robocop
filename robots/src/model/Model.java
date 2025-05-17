package model;

import model.entities.*;
import model.entities.Robot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class Model implements EntityProvider {
    private final List<Entity> entities = new ArrayList<>();
    private GameState currentGameState = GameState.PLAYING;
    private final ModelController internalController;
    private double updateDuration = 10;

    private final NavigationGrid navigationGrid;
    private final int worldWidth = 800;
    private final int worldHeight = 600;
    private final int cellWidth = 40;
    private final int cellHeight = 40;

    public Model() {
        this.internalController = new ModelController(this);
        this.navigationGrid = new NavigationGrid(worldWidth, worldHeight, cellWidth, cellHeight);
        initializeLevel();
    }

    private void initializeLevel() {
        entities.clear();
        navigationGrid.resetGrid();

        addObstacleRect(200, 200, 40, 120);
        addObstacleRect(400, 300, 120, 40);

        Robot robot = new Robot(navigationGrid);
        robot.setPosition(100, 100);
        entities.add(robot);

        entities.add(new WinPoint(500, 500));
        entities.add(new LosePoint(700, 700));

    }

    private void addObstacleRect(int worldX, int worldY, int width, int height) {
        Point topLeft = navigationGrid.worldToGrid(worldX, worldY);
        Point bottomRight = navigationGrid.worldToGrid(worldX + width -1, worldY + height -1);

        for (int r = topLeft.y; r <= bottomRight.y; r++) {
            for (int c = topLeft.x; c <= bottomRight.x; c++) {
                navigationGrid.addObstacleCell(r, c);
            }
        }
    }

    public void resetGame() {
        currentGameState = GameState.PLAYING;
        initializeLevel();
    }

    public void update(double duration) {
        if (currentGameState == GameState.PLAYING) {
            List<Entity> currentEntities = new ArrayList<>(entities);
            for (Entity entity : currentEntities) {
                entity.update(this.internalController, duration);
            }
        }
    }

    public void setRobotTargetPosition(int worldX, int worldY) {
        findFirst(e -> e instanceof Robot)
                .map(e -> (Robot) e)
                .ifPresent(robot -> {
                    double startX = robot.getPositionX();
                    double startY = robot.getPositionY();

                    List<Point> gridPath = navigationGrid.findPath(startX, startY, worldX, worldY);

                    robot.setPath(gridPath, worldX, worldY);
                });
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
            System.out.println("Game State Changed To: " + newState);
        }
    }

    public void setUpdateDuration(double duration) {
        this.updateDuration = duration;
    }

    public double getUpdateDuration() {
        return updateDuration;
    }

    public NavigationGrid getNavigationGrid() {
        return navigationGrid;
    }
}
