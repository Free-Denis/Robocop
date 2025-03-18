package model;

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
    public void update(double duration) {}

    public boolean checkLose(Robot robot) {
        double distance = GameMath.distance(x, y, robot.getPositionX(), robot.getPositionY());
        return distance < size / 2;
    }
}