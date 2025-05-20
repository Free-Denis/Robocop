package model;

import java.awt.Point;
import java.util.*;

public class NavigationGrid {

    private final int worldWidth;
    private final int worldHeight;
    private final int cellWidth;
    private final int cellHeight;
    private final int gridCols; // ширина сетки в ячейках
    private final int gridRows; // высота
    private final boolean[][] traversable;

    private static final Point[] NEIGHBOR_OFFSETS = {
            new Point(0, -1), // Вверх (row - 1)
            new Point(0, 1),  // Вниз (row + 1)
            new Point(-1, 0), // Влево (col - 1)
            new Point(1, 0)   // Вправо (col + 1)
    };

    public NavigationGrid(int worldWidth, int worldHeight, int cellWidth, int cellHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        this.gridCols = (int) Math.ceil((double) worldWidth / cellWidth);
        this.gridRows = (int) Math.ceil((double) worldHeight / cellHeight);

        this.traversable = new boolean[gridRows][gridCols];
        for (int r = 0; r < gridRows; r++) {
            Arrays.fill(traversable[r], true);
        }
    }

    public Point worldToGrid(double worldX, double worldY) {
        int col = (int) Math.floor(worldX / cellWidth);
        int row = (int) Math.floor(worldY / cellHeight);
        col = Math.max(0, Math.min(gridCols - 1, col));
        row = Math.max(0, Math.min(gridRows - 1, row));
        return new Point(col, row);
    }

    public Point gridCellCenterToWorld(Point gridCell) {
        return gridCellCenterToWorld(gridCell.y, gridCell.x);
    }

    public Point gridCellCenterToWorld(int row, int col) {
        int worldX = (int) (col * cellWidth + cellWidth / 2.0);
        int worldY = (int) (row * cellHeight + cellHeight / 2.0);
        return new Point(worldX, worldY);
    }


    public void addObstacleCell(int row, int col) {
        if (isValid(row, col)) {
            traversable[row][col] = false;
        }
    }



    public boolean isTraversable(int row, int col) {
        return isValid(row, col) && traversable[row][col];
    }


    boolean isValid(int row, int col) {
        return row >= 0 && row < gridRows && col >= 0 && col < gridCols;
    }

    public List<Point> findPath(double startWorldX, double startWorldY, double endWorldX, double endWorldY) {
        Point startCell = worldToGrid(startWorldX, startWorldY);
        Point endCell = worldToGrid(endWorldX, endWorldY);

        if (!isTraversable(endCell.y, endCell.x)) {
            return Collections.emptyList();
        }
//        if (!isTraversable(startCell.y, startCell.x)) {
//            //System.err.println("стартовая ячейка непроходима: " + startCell);
//            return Collections.emptyList();
//        }
        if (!isTraversable(endCell.y, endCell.x)) {
            //System.err.println("конечная ячейка непроходима: " + endCell);
            return Collections.emptyList();
        }

        if (startCell.equals(endCell)) {
            return Collections.emptyList();
        }

        Deque<Point> queue = new ArrayDeque<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        queue.offer(startCell);
        visited.add(startCell);
        cameFrom.put(startCell, null);

        Point current;
        boolean pathFound = false;


        while (!queue.isEmpty()) {
            current = queue.poll();

            if (current.equals(endCell)) {
                pathFound = true;
                break;
            }

            for (Point offset : NEIGHBOR_OFFSETS) {
                int nextRow = current.y + offset.y; // offset.y - это смещение по строке
                int nextCol = current.x + offset.x; // offset.x - это смещение по столбцу
                Point neighbor = new Point(nextCol, nextRow);

                if (isTraversable(nextRow, nextCol) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }

        if (pathFound) {
            LinkedList<Point> path = new LinkedList<>();
            Point step = endCell;
            while (step != null && !step.equals(startCell)) {
                path.addFirst(step);
                step = cameFrom.get(step);
            }
            return path;
        } else {
            return Collections.emptyList();
        }
    }

    public void resetGrid() {
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                traversable[r][c] = true; // все ячейки проходимы
            }
        }
    }
}
