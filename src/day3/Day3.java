package day3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static common.ReadingUtils.makeScannerFromFile;

public class Day3 {
    public static  void main(String[] args) {
        try {
            System.out.println(computePowerConsumption(new File("src/day3/input.txt")));
            System.out.println(computeLifeSupportRating(new File("src/day3/input.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int computePowerConsumption(File inFile) throws IOException {
        Scanner s = makeScannerFromFile(inFile);

        if (s != null) {
            return doComputePowerConsumption(makeReportsList(Files.readAllLines(inFile.toPath())));
        }

        return -1;
    }

    private static ArrayList<ArrayList<Integer>> makeReportsList(List<String> reportStrings) {
        String report = reportStrings.get(0);
        ArrayList<ArrayList<Integer>> reports = initializeRatesArray(report.length());

        for (String r : reportStrings) {
            for (int i = 0; i < r.length(); i++) {
                char digit = r.charAt(i);

                if (digit == '0') {
                    reports.get(i).set(0, reports.get(i).get(0) + 1);
                } else {
                    reports.get(i).set(1, reports.get(i).get(1) + 1);
                }
            }
        }

        return reports;
    }

    private static int computeLifeSupportRating(File inFile) throws IOException {
        List<String> reportStrings = Files.readAllLines(inFile.toPath());
        int oxygenRating = reduceToSingleReport(reportStrings, true);
        int co2Rating = reduceToSingleReport(reportStrings, false);

        return oxygenRating * co2Rating;
    }

    private static int reduceToSingleReport(List<String> reportStrings, boolean useMostCommon) {
        int sizeOfReport = reportStrings.get(0).length();
        ArrayList<ArrayList<Integer>> reports;

        for (int i = 0; i < sizeOfReport && reportStrings.size() > 1; i++) {
            reports = makeReportsList(reportStrings);
            int mostCommonBit, leastCommonBit;
            if (reports.get(i).get(0) > reports.get(i).get(1)) {
                mostCommonBit = 0;
                leastCommonBit = 1;
            } else {
                mostCommonBit = 1;
                leastCommonBit = 0;
            }

            int index = i;
            reportStrings = reportStrings.stream().filter((rs) -> {
                if (useMostCommon) {
                    return Integer.parseInt(Character.toString(rs.charAt(index))) == mostCommonBit;
                } else {
                    return Integer.parseInt(Character.toString(rs.charAt(index))) == leastCommonBit;
                }
            }).toList();
        }

        return Integer.parseInt(reportStrings.get(0), 2);
    }


    private static int doComputePowerConsumption(ArrayList<ArrayList<Integer>> reports) {
        StringBuilder gammaBuilder = new StringBuilder();
        StringBuilder epsilonBuilder = new StringBuilder();

        for (ArrayList<Integer> report : reports) {
            // As long as there's always an odd number of reports this should work lol
            if (report.get(0) > report.get(1)) {
                gammaBuilder.append(0);
                epsilonBuilder.append(1);
            } else {
                gammaBuilder.append(1);
                epsilonBuilder.append(0);
            }
        }

        return Integer.parseInt(gammaBuilder.toString(), 2) * Integer.parseInt(epsilonBuilder.toString(), 2);
    }

    private static ArrayList<ArrayList<Integer>> initializeRatesArray(int size) {
        ArrayList<ArrayList<Integer>> rates = new ArrayList<>(size);

        for (int i = 0; i < size; i++){
            ArrayList<Integer> initial = new ArrayList<>(2);
            initial.add(0);
            initial.add(0);
            rates.add(initial);
        }

        return rates;
    }
}
