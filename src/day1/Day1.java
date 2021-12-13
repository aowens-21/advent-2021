package day1;

import java.io.File;
import java.util.Scanner;

import static common.ReadingUtils.makeScannerFromFile;

public class Day1 {
    public static void main(String[] args) {
        System.out.println(countIncreasingMeasurements(new File("src/day1/input1.txt")));
        System.out.println(countIncreasingSums(new File("src/day1/input1.txt")));
    }

    private static int countIncreasingSums(File inFile) {
        Scanner s = makeScannerFromFile(inFile);

        if (s != null) {
            int increasingSums = 0;
            int oldestMeasurement = Integer.parseInt(s.nextLine());
            int secondOldestMeasurement = Integer.parseInt(s.nextLine());
            int thirdOldestMeasurement = Integer.parseInt(s.nextLine());

            while (s.hasNextLine()) {
                int prevSum = oldestMeasurement + secondOldestMeasurement + thirdOldestMeasurement;
                int newMeasurement = Integer.parseInt(s.nextLine());
                int currSum = prevSum - oldestMeasurement + newMeasurement;

                if (currSum > prevSum) {
                    increasingSums += 1;
                }

                oldestMeasurement = secondOldestMeasurement;
                secondOldestMeasurement = thirdOldestMeasurement;
                thirdOldestMeasurement = newMeasurement;
            }

            return increasingSums;
        }

        return -1;
    }

    private static int countIncreasingMeasurements(File inFile) {
        Scanner s = makeScannerFromFile(inFile);

        if (s != null) {
            int increasingMeasurements = 0;
            int preMeasurement = Integer.parseInt(s.nextLine());

            while (s.hasNextLine()) {
                int currMeasurement = Integer.parseInt(s.nextLine());

                if (currMeasurement > preMeasurement) {
                    increasingMeasurements += 1;
                }

                preMeasurement = currMeasurement;
            }

            return increasingMeasurements;
        }

        return -1;
    }
}
