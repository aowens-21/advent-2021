package day2;

import java.io.File;
import java.util.Scanner;

import static common.ReadingUtils.makeScannerFromFile;

public class Day2 {
    public static void main(String[] args) {
        System.out.println(computePartOneAnswer(new File("src/day2/input.txt")));
        System.out.println(computePartTwoAnswer(new File("src/day2/input.txt")));
    }

    private static int computePartOneAnswer(File inFile) {
        Scanner s = makeScannerFromFile(inFile);

        if (s != null) {
            int totalDepth = 0;
            int totalHorizontalPosition = 0;

            while (s.hasNextLine()) {
                SubmarineCommand currCmd = new SubmarineCommand(s.nextLine());

                switch (currCmd.getCmdType()) {
                    case FORWARD -> totalHorizontalPosition += currCmd.getMagnitude();
                    case UP -> totalDepth -= currCmd.getMagnitude();
                    case DOWN -> totalDepth += currCmd.getMagnitude();
                }
            }

            return totalDepth * totalHorizontalPosition;
        }

        return -1;
    }

    private static int computePartTwoAnswer(File inFile) {
        Scanner s = makeScannerFromFile(inFile);

        if (s != null) {
            int totalDepth = 0;
            int totalHorizontalPosition = 0;
            int aim = 0;

            while (s.hasNextLine()) {
                SubmarineCommand currCmd = new SubmarineCommand(s.nextLine());

                switch (currCmd.getCmdType()) {
                    case FORWARD -> {
                        int mag = currCmd.getMagnitude();
                        totalHorizontalPosition += mag;
                        totalDepth += aim * mag;
                    }
                    case UP -> aim -= currCmd.getMagnitude();
                    case DOWN -> aim += currCmd.getMagnitude();
                }
            }

            return totalDepth * totalHorizontalPosition;
        }

        return -1;
    }
}

enum Type { FORWARD, UP, DOWN }

class SubmarineCommand {
    private Type cmdType;
    private int magnitude;

    public SubmarineCommand(String commandAsStr) {
        try {
            this.cmdType = parseCmdType(commandAsStr);
            this.magnitude = parseMagnitude(commandAsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type getCmdType() {
        return cmdType;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public static Type parseCmdType(String str) throws Exception{
        String typeStr = str.split(" ")[0];

        switch (typeStr) {
            case "forward":
                return Type.FORWARD;
            case "up":
                return Type.UP;
            case "down":
                return Type.DOWN;
        }

        throw new Exception(String.format("Invalid type provided in: %s", typeStr));
    }

    public static int parseMagnitude(String str) {
        String magStr = str.split(" ")[1];

        return Integer.parseInt(magStr);
    }
}
