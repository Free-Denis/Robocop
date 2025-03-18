package model;

public class Robot implements Entity {
    private volatile double positionX = 100;
    private volatile double positionY = 100;
    private volatile double direction = 0;

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    public void setTargetPosition(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
    }

    @Override
    public void update(double duration) {
        double distance = GameMath.distance(targetPositionX, targetPositionY, positionX, positionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = GameMath.angleTo(positionX, positionY, targetPositionX, targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < direction) {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, duration);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = GameMath.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = GameMath.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) -
                        Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = positionX + velocity * duration * Math.cos(direction);
        }
        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) -
                        Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = positionY + velocity * duration * Math.sin(direction);
        }
        positionX = newX;
        positionY = newY;
        double newDirection = GameMath.asNormalizedRadians(direction + angularVelocity * duration);
        direction = newDirection;
    }

    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }
}