package day8;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day8 {
    public static void main(String[] args) {
        try {
            List<HashMap<Character, POSITION>> possibleConfigs = generateAllPossibleConfigurationMaps();
            System.out.println(countDigitsWithUniqueSegmentLengthsInOutputValues(new File("src/day8/input.txt")));
            System.out.println(parseEntriesFromFile(new File("src/day8/input.txt")).stream().mapToInt(e -> decodeEntry(e, possibleConfigs)).sum());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int countDigitsWithUniqueSegmentLengthsInOutputValues(File inFile) throws IOException {
        List<Entry> entries = parseEntriesFromFile(inFile);
        int totalDigits = 0;

        for (Entry e : entries) {
            String[] outputValues = e.getOutputValues();

            for (String o : outputValues) {
                switch (o.length()) {
                    case 2, 3, 4, 7 -> totalDigits += 1;
                }
            }
        }

        return totalDigits;
    }

    private static int decodeEntry(Entry e, List<HashMap<Character, POSITION>> possibleConfigs) {
        HashMap<Character, POSITION> config = computeValidConfig(e.getSignalPatterns(), possibleConfigs);
        StringBuilder valStr = new StringBuilder("");

        for (String s : e.getOutputValues()) {
            valStr.append(VALID_DIGITS.get(makeDigitSetFromString(s, config)));
        }

        return Integer.parseInt(valStr.toString());
    }

    private static HashMap<Character, POSITION> computeValidConfig(String[] signalPatterns, List<HashMap<Character, POSITION>> possibleConfigs) {
        for (HashMap<Character, POSITION> config : possibleConfigs) {
            boolean isValid = true;

            for (String s : signalPatterns) {
                if (!VALID_DIGITS.containsKey(makeDigitSetFromString(s, config))) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                return config;
            }
        }

        return null;
    }

    private static Set<POSITION> makeDigitSetFromString(String s, HashMap<Character, POSITION> config) {
        Set<POSITION> digitSet = new HashSet<>();
        s.chars().mapToObj(i -> (char)i).forEach(c -> digitSet.add(config.get(c)));
        return digitSet;
    }

    private static List<Entry> parseEntriesFromFile(File inFile) throws IOException {
        Scanner s = new Scanner(inFile);
        List<Entry> entries = new ArrayList<>();

        while (s.hasNextLine()) {
            entries.add(new Entry(s.nextLine()));
        }

        return entries;
    }

    private static List<HashMap<Character, POSITION>> generateAllPossibleConfigurationMaps() {
        ArrayList<Character> segmentNames = new ArrayList<>();
        segmentNames.add('a');
        segmentNames.add('b');
        segmentNames.add('c');
        segmentNames.add('d');
        segmentNames.add('e');
        segmentNames.add('f');
        segmentNames.add('g');

        List<HashMap<Character, POSITION>> configs = new ArrayList<>();

        recursivelyGenerateConfigs(segmentNames, new HashMap<>(), configs);

        return configs;
    }

    private static void recursivelyGenerateConfigs(ArrayList<Character> remainingNames,
                                                   HashMap<Character, POSITION> currentL,
                                                   List<HashMap<Character, POSITION>> configs) {
        if (remainingNames.isEmpty()) {
            configs.add(currentL);
        } else {
            for (Character c : remainingNames) {
                HashMap<Character, POSITION> currentClone = (HashMap<Character, POSITION>) currentL.clone();
                ArrayList<Character> remainClone = (ArrayList<Character>) remainingNames.clone();

                currentClone.put(c, POSITION.ordinals[currentClone.keySet().size()]);
                remainClone.remove(c);
                recursivelyGenerateConfigs(remainClone, currentClone, configs);
            }
        }
    }

    private static final Map<Set<POSITION>, Integer> VALID_DIGITS = Map.of(
            Set.of(POSITION.T, POSITION.TL, POSITION.TR, POSITION.BR, POSITION.BL, POSITION.B), 0,
            Set.of(POSITION.TR, POSITION.BR), 1,
            Set.of(POSITION.T, POSITION.TR, POSITION.M, POSITION.BL, POSITION.B), 2,
            Set.of(POSITION.T, POSITION.M, POSITION.B, POSITION.TR, POSITION.BR), 3,
            Set.of(POSITION.TL, POSITION.TR, POSITION.M, POSITION.BR), 4,
            Set.of(POSITION.T, POSITION.TL, POSITION.M, POSITION.BR, POSITION.B), 5,
            Set.of(POSITION.T, POSITION.TL, POSITION.M, POSITION.BR, POSITION.B, POSITION.BL), 6,
            Set.of(POSITION.T, POSITION.TR, POSITION.BR), 7,
            Set.of(POSITION.T, POSITION.TL, POSITION.TR, POSITION.M, POSITION.BR, POSITION.BL, POSITION.B), 8,
            Set.of(POSITION.T, POSITION.TL, POSITION.TR, POSITION.M, POSITION.BR, POSITION.B), 9);
}

class Entry {
    private final String[] signalPatterns;
    private final String[] outputValues;

    public Entry(String entryStr) {
        String[] twoParts = entryStr.split("\\|");
        signalPatterns = Arrays.stream(twoParts[0].split(" ")).filter(s -> !s.equals(" ")).toArray(String[]::new);
        outputValues = Arrays.stream(twoParts[1].split(" ")).filter(s -> !s.equals(" ") && !s.equals("")).toArray(String[]::new);
    }

    public String[] getSignalPatterns() {
        return signalPatterns;
    }

    public String[] getOutputValues() {
        return outputValues;
    }
}

enum POSITION {
    T,
    TL,
    TR,
    M,
    BL,
    BR,
    B;
    public static final POSITION ordinals[] = values();
}

