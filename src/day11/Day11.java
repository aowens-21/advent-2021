package day11;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 {

    public static void main(String[] args) {
        try {
            System.out.println(doSteps(parseGridFromFile(new File("src/day11/input.txt")), 100));
            System.out.println(getFirstSynchronizedStep(parseGridFromFile(new File("src/day11/input.txt"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getFirstSynchronizedStep(List<List<Integer>> grid) {
        int stepNum = 0;

        while (!allFlashing(grid)) {
            doStep(grid);
            stepNum += 1;
        }

        return stepNum;
    }

    private static boolean allFlashing(List<List<Integer>> grid) {
        for (List<Integer> row : grid) {
            for (Integer i : row) {
                if (i != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int doSteps(List<List<Integer>> grid, int stepCount) {
        int total = 0;

        for (int i = 0; i < stepCount; i++) {
            total += doStep(grid);
        }

        return total;
    }

    private static int doStep(List<List<Integer>> grid) {
        List<Point> initial = findInitialFlashingSpots(grid);
        propagateFlashes(initial, grid);
        return countAndResetFlashes(grid);
    }

    private static int countAndResetFlashes(List<List<Integer>> grid) {
        int flashes = 0;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                if (grid.get(i).get(j) > 9) {
                    flashes += 1;
                    grid.get(i).set(j, 0);
                }
            }
        }

        return flashes;
    }

    private static void propagateFlashes(List<Point> initialFlashingPoints, List<List<Integer>> grid) {
        Queue<Point> flashingPoints = new LinkedList<>(initialFlashingPoints);

        while (!flashingPoints.isEmpty()) {
            Point currentPoint = flashingPoints.remove();
            List<Point> neighbors = getNeighborsInBounds(currentPoint, grid);

            for (Point p : neighbors) {
                int newVal = grid.get(p.getX()).get(p.getY()) + 1;

                if (newVal == 10) {
                    flashingPoints.add(p);
                }

                grid.get(p.getX()).set(p.getY(), newVal);
            }
        }
    }

    private static List<Point> findInitialFlashingSpots(List<List<Integer>> energyGrid) {
        List<Point> flashingPoints = new ArrayList<>();

        for (int i = 0; i < energyGrid.size(); i++) {
            for (int j = 0; j < energyGrid.get(0).size(); j++) {
                int newEnergy = energyGrid.get(i).get(j) + 1;
                energyGrid.get(i).set(j, newEnergy);

                if (newEnergy > 9) {
                    flashingPoints.add(new Point(i, j));
                }
            }
        }

        return flashingPoints;
    }

    private static List<List<Integer>> parseGridFromFile(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        List<List<Integer>> grid = new ArrayList<>();

        while (s.hasNextLine()) {
            String cl = s.nextLine();
            Character[] clChars = cl.chars().mapToObj(i -> (char)i).toArray(Character[]::new);
            List<Integer> row = new ArrayList<>();

            for (Character c : clChars) {
                row.add(Integer.parseInt(c.toString()));
            }

            grid.add(row);
        }

        return grid;
    }

    private static List<Point> getNeighborsInBounds(Point currentPoint, List<List<Integer>> grid) {
        Point upLeftNeighbor = new Point(currentPoint.getX() - 1, currentPoint.getY() - 1);
        Point upNeighbor = new Point(currentPoint.getX() - 1, currentPoint.getY());
        Point upRightNeighbor = new Point(currentPoint.getX() - 1, currentPoint.getY() + 1);
        Point leftNeighbor = new Point(currentPoint.getX(), currentPoint.getY() - 1);
        Point rightNeighbor = new Point(currentPoint.getX(), currentPoint.getY() + 1);
        Point downLeftNeighbor = new Point(currentPoint.getX() + 1, currentPoint.getY() - 1);
        Point downNeighbor = new Point(currentPoint.getX() + 1, currentPoint.getY());
        Point downRightNeighbor = new Point(currentPoint.getX() + 1, currentPoint.getY() + 1);

        return Stream.of(upLeftNeighbor,
                upRightNeighbor,
                upNeighbor,
                leftNeighbor,
                rightNeighbor,
                downLeftNeighbor,
                downNeighbor,
                downRightNeighbor).filter(p -> isInBounds(p, grid)).collect(Collectors.toList());
    }

    private static boolean isInBounds(Point p, List<List<Integer>> grid) {
        return p.getX() >= 0 && p.getX() < grid.size() && p.getY() >= 0 && p.getY() < grid.get(0).size();
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        day11.Point point = (day11.Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
