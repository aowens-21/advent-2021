package day5;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {
    public static void main(String[] args) {
        try {
            System.out.println(computeOverlappingPoints(new File("src/day5/input.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int computeOverlappingPoints(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        HashMap<Point, Integer> pointToCountMap = buildPointToCountMap(s);

        int overlapping = 0;

        for (Point p : pointToCountMap.keySet()) {
            if (pointToCountMap.get(p) > 1) {
                overlapping += 1;
            }
        }

        return overlapping;
    }

    private static HashMap<Point, Integer> buildPointToCountMap(Scanner lineScanner) {
        HashMap<Point, Integer> m = new HashMap<>();

        while (lineScanner.hasNextLine()) {
            String currLine = lineScanner.nextLine();
            List<Point> linePoints = getPointsOnLine(Arrays.stream(currLine.split(" -> ")).map(pointStr ->
                    new Point(Integer.parseInt(pointStr.split(",")[0]),
                              Integer.parseInt(pointStr.split(",")[1])))
                    .toList());

            for (Point p : linePoints) {
                if (m.containsKey(p)) {
                    m.replace(p, m.get(p) + 1);
                } else {
                    m.put(p, 1);
                }
            }
        }

        return m;
    }

    private static List<Point> getPointsOnLine(List<Point> endpoints) {
        int startX = endpoints.get(0).getX();
        int startY = endpoints.get(0).getY();
        int endX = endpoints.get(1).getX();
        int endY = endpoints.get(1).getY();
        ArrayList<Point> points = new ArrayList<>();


        if (startX == endX) {
            if (startY < endY) {
                for (int i = startY; i <= endY; i++) {
                    points.add(new Point(startX, i));
                }
            } else {
                for (int i = endY; i <= startY; i++) {
                    points.add(new Point(startX, i));
                }
            }
        } else if (startY == endY) {
            if (startX < endX) {
                for (int i = startX; i <= endX; i++) {
                    points.add(new Point(i, startY));
                }
            } else {
                for (int i = endX; i <= startX; i++) {
                    points.add(new Point(i, startY));
                }
            }
        } else {
            // congrats we have a diagonal line
            // need each coordinate to "work towards" the goal
            if (startX < endX && startY < endY) {
                for (int i = 0; i <= endX - startX; i++) {
                    points.add(new Point(startX + i, startY + i));
                }
            } else if (startX < endX && startY > endY) {
                for (int i = 0; i <= endX - startX; i++) {
                    points.add(new Point(startX + i, startY - i));
                }
            } else if (startX > endX && startY < endY) {
                for (int i = 0; i <= endY - startY; i++) {
                    points.add(new Point(startX - i, startY + i));
                }
            } else {
                for (int i = 0; i <= startX - endX; i++) {
                    points.add(new Point(startX - i, startY - i));
                }
            }
        }

        return points;
    }
}



class Point {
    private int x;
    private int y;

    public Point (int x, int y) {
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
