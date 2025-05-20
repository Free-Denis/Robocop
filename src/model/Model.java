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
    private final TimerModel timerModel;
    private final NavigationGrid navigationGrid;
    private final int worldWidth = 1900;
    private final int worldHeight = 1000;
    private final int cellWidth = 20;
    private final int cellHeight = 20;

    public Model() {
        this.internalController = new ModelController(this);
        this.navigationGrid = new NavigationGrid(worldWidth, worldHeight, cellWidth, cellHeight);
        initializeLevel();
        this.timerModel = new TimerModel();
        resetGame();
    }
    private void generateRandomObstacles(int count) {
        Random random = new Random();

        for (int i = 0; i < count; i++) {

            int x = random.nextInt(worldWidth / cellWidth) * cellWidth;
            int y = random.nextInt(worldHeight / cellHeight) * cellHeight;
            int width = cellWidth;
            int height = cellHeight;


            Obstacle obstacle = new Obstacle(x, y, width, height);
            entities.add(obstacle);


            Point gridCell = navigationGrid.worldToGrid(x, y);
            navigationGrid.addObstacleCell(gridCell.y, gridCell.x);
        }
    }
    private void initializeLevel() {
        entities.clear();
        navigationGrid.resetGrid();
        generateRandomObstacles(1200);
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
        timerModel.reset();
        initializeLevel();
    }

    public void update(double duration) {
        if (currentGameState == GameState.PLAYING) {
            if (timerModel.isTimeExpired()) {
                setGameState(GameState.DEFEAT);
                return;
            }
            List<Entity> currentEntities = new ArrayList<>(entities);
            for (Entity entity : currentEntities) {
                entity.update(this.internalController, duration);
            }
        }
    }
    public TimerModel getTimerModel() {
        return timerModel;
    }
    public void setRobotTargetPosition(int worldX, int worldY) {
        findFirst(e -> e instanceof Robot)
                .map(e -> (Robot) e)
                .ifPresent(robot -> {
                    double startX = robot.getPositionX();
                    double startY = robot.getPositionY();


                    Point targetCell = navigationGrid.worldToGrid(worldX, worldY);
                    if (!navigationGrid.isTraversable(targetCell.y, targetCell.x)) {
                        return;
                    }

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
