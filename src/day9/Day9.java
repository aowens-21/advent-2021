package day9;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) {
        try {
            List<List<Integer>> heightMap = parseHeightMapFromLines(new File("src/day9/input.txt"));
            System.out.println(sumRiskLevelsOfLowPoints(heightMap));
            System.out.println(multiplyThreeLargestBasinSizes(heightMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int sumRiskLevelsOfLowPoints(List<List<Integer>> heightMap) {
        List<Point> lowPoints = getLowPoints(heightMap);
        return lowPoints.stream().mapToInt(p -> heightMap.get(p.getX()).get(p.getY()) + 1).sum();
    }

    private static int multiplyThreeLargestBasinSizes(List<List<Integer>> heightMap) {
        List<Integer> basinSizes = getBasinSizes(heightMap).stream().sorted().toList();
        return basinSizes.get(basinSizes.size() - 1) * basinSizes.get(basinSizes.size() - 2) * basinSizes.get(basinSizes.size() - 3);
    }

    private static List<Integer> getBasinSizes(List<List<Integer>> heightMap) {
        List<Point> lowPoints = getLowPoints(heightMap);
        List<Integer> sizes = new ArrayList<>();

        for (Point p : lowPoints) {
            sizes.add(getSizeOfBasinFromPoint(p, heightMap));
        }

        return sizes;
    }

    private static Integer getSizeOfBasinFromPoint(Point lpOfBasin, List<List<Integer>> heightMap) {
        Queue<Point> pointsToExplore = new LinkedList<>();
        Set<Point> exploredPoints = new HashSet<>();
        int size = 1;
        pointsToExplore.add(lpOfBasin);
        exploredPoints.add(lpOfBasin);

        while (!pointsToExplore.isEmpty()) {
            Point cp = pointsToExplore.remove();

            int cpX = cp.getX();
            int cpY = cp.getY();

            if (cpX > 0 && heightMap.get(cpX - 1).get(cpY) != 9) {
                Point newPoint = new Point(cpX - 1, cpY);

                if (!exploredPoints.contains(newPoint)) {
                    size += 1;
                    pointsToExplore.add(newPoint);
                    exploredPoints.add(newPoint);
                }
            }

            if (cpX < heightMap.size() - 1 && heightMap.get(cpX + 1).get(cpY) != 9) {
                Point newPoint = new Point(cpX + 1, cpY);

                if (!exploredPoints.contains(newPoint)) {
                    size += 1;
                    pointsToExplore.add(newPoint);
                    exploredPoints.add(newPoint);
                }
            }

            if (cpY > 0 && heightMap.get(cpX).get(cpY - 1) != 9) {
                Point newPoint = new Point(cpX, cpY - 1);

                if (!exploredPoints.contains(newPoint)) {
                    size += 1;
                    pointsToExplore.add(newPoint);
                    exploredPoints.add(newPoint);
                }
            }

            if (cpY < heightMap.get(0).size() - 1 && heightMap.get(cpX).get(cpY + 1) != 9) {
                Point newPoint = new Point(cpX, cpY + 1);

                if (!exploredPoints.contains(newPoint)) {
                    size += 1;
                    pointsToExplore.add(newPoint);
                    exploredPoints.add(newPoint);
                }
            }
        }

        return size;
    }

    private static List<Point> getLowPoints(List<List<Integer>> heightMap) {
        int numOfRows = heightMap.size();
        int rowLength = heightMap.get(0).size();
        List<Point> lowPoints = new ArrayList<>();

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < rowLength; j++) {
                int currentHeight = heightMap.get(i).get(j);
                boolean isLowPoint = true;

                if (i > 0) {
                    if (currentHeight >= heightMap.get(i - 1).get(j)) {
                        isLowPoint = false;
                    }
                }

                if (i < numOfRows - 1) {
                    if (currentHeight >= heightMap.get(i + 1).get(j)) {
                        isLowPoint = false;
                    }
                }

                if (j > 0) {
                    if (currentHeight >= heightMap.get(i).get(j - 1)) {
                        isLowPoint = false;
                    }
                }

                if (j < rowLength - 1) {
                    if (currentHeight >= heightMap.get(i).get(j + 1)) {
                        isLowPoint = false;
                    }
                }

                if (isLowPoint) {
                    lowPoints.add(new Point(i, j));
                }
            }
        }

        return lowPoints;
    }

    private static List<List<Integer>> parseHeightMapFromLines(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        List<List<Integer>> heightMapList = new ArrayList<>();

        while (s.hasNextLine()) {
            String cl = s.nextLine();
            Character[] clChars = cl.chars().mapToObj(i -> (char)i).toArray(Character[]::new);
            List<Integer> row = new ArrayList<>();

            for (Character c : clChars) {
                row.add(Integer.parseInt(c.toString()));
            }

            heightMapList.add(row);
        }

        return heightMapList;
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
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
