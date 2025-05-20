package model.entities;

import model.ModelController;
import model.GameMath;
import model.NavigationGrid;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Robot implements Entity {
    private volatile double positionX = 100;
    private volatile double positionY = 100;
    private volatile double direction = 0;

    private final NavigationGrid navigationGrid;
    private List<Point> currentGridPath = Collections.emptyList();
    private int currentPathIndex = -1;

    private volatile double intermediateTargetX;
    private volatile double intermediateTargetY;
    private volatile int finalTargetX;
    private volatile int finalTargetY;
    private volatile boolean hasTarget = false;

    private static final double maxVelocity = 0.45;
    private static final double maxAngularVelocity = 0.02;

    private static final double TARGET_REACH_THRESHOLD = 3.0; // дистанция для достижения точки
    private static final double FINAL_TARGET_REACH_THRESHOLD = 1.5; // более точный порог для конечной точки
    private static final double ANGLE_ALIGN_THRESHOLD = 0.03; // порог для выравнивания
    private static final double DECELERATION_DISTANCE = maxVelocity * 400; // дистанция для начала замедления
    private static final double MIN_VELOCITY_FACTOR = 0.2; // мин скорость как доля от макс
    private static final double TURN_PRIORITY_ANGLE = Math.PI / 3.0; // снижаем скорость для поворота
    private static final double SLOW_DOWN_TURN_ANGLE = Math.PI / 4.0; // снижаем скорость

    public Robot(NavigationGrid grid) {
        this.navigationGrid = grid;
    }

    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getDirection() { return direction; }
    public Point getFinalTarget() { return hasTarget ? new Point(finalTargetX, finalTargetY) : null; }


    @Override
    public void update(ModelController controller, double duration) {
        if (!hasTarget || duration <= 0) {
            return;
        }


        double currentTargetReachThreshold = TARGET_REACH_THRESHOLD;
        boolean isTargetingFinal = (intermediateTargetX == finalTargetX && intermediateTargetY == finalTargetY);
        if (isTargetingFinal) {
            currentTargetReachThreshold = FINAL_TARGET_REACH_THRESHOLD;
        }

        double distance = GameMath.distance(intermediateTargetX, intermediateTargetY, positionX, positionY);
        double angleToTarget = GameMath.angleTo(positionX, positionY, intermediateTargetX, intermediateTargetY);
        double angleDifference = angleToTarget - direction;

        while (angleDifference <= -Math.PI) angleDifference += 2 * Math.PI;
        while (angleDifference > Math.PI) angleDifference -= 2 * Math.PI;


        if (distance < currentTargetReachThreshold) {

            boolean switchedTarget = false;

            if (currentPathIndex < currentGridPath.size() - 1) {
                currentPathIndex++;
                Point nextGridCell = currentGridPath.get(currentPathIndex);
                Point nextWorldTarget = navigationGrid.gridCellCenterToWorld(nextGridCell);
                setIntermediateTarget(nextWorldTarget.x, nextWorldTarget.y);
                switchedTarget = true;
            } else {
                if (!isTargetingFinal) {
                    setIntermediateTarget(finalTargetX, finalTargetY);
                    switchedTarget = true;
                } else {
                    if (distance < FINAL_TARGET_REACH_THRESHOLD) {
                        hasTarget = false;
                        currentGridPath = Collections.emptyList();
                        currentPathIndex = -1;
                        return;
                    }
                }
            }

            if (switchedTarget) {
                distance = GameMath.distance(intermediateTargetX, intermediateTargetY, positionX, positionY);
                angleToTarget = GameMath.angleTo(positionX, positionY, intermediateTargetX, intermediateTargetY);
                angleDifference = angleToTarget - direction;
                while (angleDifference <= -Math.PI) angleDifference += 2 * Math.PI;
                while (angleDifference > Math.PI) angleDifference -= 2 * Math.PI;
                if (intermediateTargetX != finalTargetX || intermediateTargetY != finalTargetY) {
                    isTargetingFinal = false;
                }
            }
        }

        if (!hasTarget) return;

        double velocity = maxVelocity;
        if (distance < DECELERATION_DISTANCE) {
            velocity = maxVelocity * (distance / DECELERATION_DISTANCE);
            velocity = Math.max(velocity, maxVelocity * MIN_VELOCITY_FACTOR);
        }

        double angularVelocity = 0;
        if (Math.abs(angleDifference) > ANGLE_ALIGN_THRESHOLD) {
            angularVelocity = Math.copySign(maxAngularVelocity, angleDifference);

            double maxAngleChangePerTick = maxAngularVelocity * duration;
            if (Math.abs(angleDifference) < maxAngleChangePerTick) {
                angularVelocity = angleDifference / duration;
                angularVelocity = GameMath.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
            }
        } else {
            angularVelocity = 0;
        }

        double turnFactor = 1.0;
        if (Math.abs(angleDifference) > TURN_PRIORITY_ANGLE) {
            turnFactor = 0.6;
        } else if (Math.abs(angleDifference) > SLOW_DOWN_TURN_ANGLE) {
            turnFactor = 0.8;
        }

        velocity *= turnFactor;

        if (turnFactor < 1.0 && Math.abs(angleDifference) > ANGLE_ALIGN_THRESHOLD) {
            velocity = Math.max(velocity, maxVelocity * MIN_VELOCITY_FACTOR * 0.5);
        }

        moveRobot(velocity, angularVelocity, duration);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        double newDirection = GameMath.asNormalizedRadians(direction + angularVelocity * duration);
        double moveX, moveY;
        double avgDirection = GameMath.asNormalizedRadians(direction + angularVelocity * duration / 2.0);
        moveX = velocity * duration * Math.cos(avgDirection);
        moveY = velocity * duration * Math.sin(avgDirection);

        if (Double.isFinite(moveX) && Double.isFinite(moveY)) {
            positionX += moveX;
            positionY += moveY;
        }

        direction = newDirection;
    }


    public void setPath(List<Point> gridPath, int finalWorldX, int finalWorldY) {
        this.currentGridPath = gridPath == null ? Collections.emptyList() : new ArrayList<>(gridPath);
        this.finalTargetX = finalWorldX;
        this.finalTargetY = finalWorldY;
        this.hasTarget = false;

        if (!this.currentGridPath.isEmpty()) {
            this.currentPathIndex = 0;
            Point firstCell = this.currentGridPath.get(0);
            Point firstWorldTarget = navigationGrid.gridCellCenterToWorld(firstCell);
            setIntermediateTarget(firstWorldTarget.x, firstWorldTarget.y);
            this.hasTarget = true;
        } else {

            double distToFinal = GameMath.distance(finalWorldX, finalWorldY, positionX, positionY);
            if (distToFinal <= FINAL_TARGET_REACH_THRESHOLD) {

                this.hasTarget = false;
            } else {

                this.hasTarget = false;
                this.currentGridPath = Collections.emptyList();
            }
        }
    }

    private void setIntermediateTarget(double x, double y) {
        this.intermediateTargetX = x;
        this.intermediateTargetY = y;
    }

    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }
}