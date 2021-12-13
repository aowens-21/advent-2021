package day7;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) {
        try {
           System.out.println(computeLeastFuelSpentToAlign(new File("src/day7/input.txt"), (i) -> i));
           System.out.println(computeLeastFuelSpentToAlign(new File("src/day7/input.txt"), (i) -> ((i * (i + 1)) / 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int computeLeastFuelSpentToAlign(File inFile, IntUnaryOperator fuelConsumptionFunc) throws IOException {
        Scanner s = new Scanner(inFile);
        Integer[] crabPositions = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        List<Integer> cPosList = Arrays.stream(crabPositions).sorted().toList();

        int upperBound = cPosList.get(cPosList.size() - 1);
        int lowerBound = cPosList.get(0);

        int leastFuelSpent = -1;

        for (int targetPos = lowerBound; targetPos <= upperBound; targetPos++) {
            int currentFuelSpent = 0;

            for (Integer pos : crabPositions) {
                if (pos > targetPos) {
                    currentFuelSpent += fuelConsumptionFunc.applyAsInt((pos - targetPos));
                } else if (pos < targetPos) {
                    currentFuelSpent += fuelConsumptionFunc.applyAsInt((targetPos - pos));
                }

                if (leastFuelSpent != -1 && currentFuelSpent >= leastFuelSpent) {
                    break;
                }
            }

            if (leastFuelSpent == -1 || currentFuelSpent < leastFuelSpent) {
                leastFuelSpent = currentFuelSpent;
            }
        }

        return leastFuelSpent;
    }
}
