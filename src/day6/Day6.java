package day6;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) {
        try {
            System.out.println(computeFishAfterEightyDays(new File("src/day6/input.txt")));
            System.out.println(computeFishAfterTwoFiftySixDays(new File("src/day6/input.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long computeFishAfterEightyDays(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        long[] fishNums = Arrays.stream(s.nextLine().split(",")).mapToLong(Long::parseLong).toArray();
        return computeFishAfterDaysEfficiently(fishNums, 80);
    }

    private static long computeFishAfterTwoFiftySixDays(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        long[] fishNums = Arrays.stream(s.nextLine().split(",")).mapToLong(Long::parseLong).toArray();
        return computeFishAfterDaysEfficiently(fishNums, 256);
    }

    private static long computeFishAfterDays(long[] fishNums, int daysToRun) {
        List<Long> fishArray = Arrays.stream(fishNums)
                                        .boxed()
                                        .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < daysToRun; i++) {
            ArrayList<Long> existingFish = new ArrayList<>();
            ArrayList<Long> newFishArray = new ArrayList<>();

            for (Long fish : fishArray) {
                if (fish == 0L) {
                    // reset and add new fish
                    newFishArray.add(8L);
                    existingFish.add(6L);
                } else {
                    existingFish.add(fish - 1);
                }
            }

            existingFish.addAll(newFishArray);

            fishArray.clear();
            fishArray.addAll(existingFish);
        }

        return fishArray.size();
    }

    private static long computeFishAfterDaysEfficiently(long[] fishNums, int daysToRun) {
        HashMap<Long, Long> fishStateMap = new HashMap<>();

        // map is built just update it
        for (long fNum : fishNums) {
            if (fishStateMap.containsKey(fNum)) {
                fishStateMap.put(fNum, fishStateMap.get(fNum) + 1);
            } else {
                fishStateMap.put(fNum, 1L);
            }
        }

        for (int d = 0; d < daysToRun; d++) {
            HashMap<Long, Long> newFishStateMap = new HashMap<>();

            for (long i = 0L; i < 9L; i++) {
                if (i == 6L) {
                    long total = fishStateMap.getOrDefault(i + 1, 0L) + fishStateMap.getOrDefault(0L, 0L);
                    newFishStateMap.put(i, total);
                } else if (i == 8L) {
                    newFishStateMap.put(i, fishStateMap.getOrDefault(0L, 0L));
                } else {
                    newFishStateMap.put(i, fishStateMap.getOrDefault(i + 1, 0L));
                }
            }

            fishStateMap = (HashMap<Long, Long>) newFishStateMap.clone();
        }

        return fishStateMap.values().stream().reduce(0L, Long::sum);
    }
}
