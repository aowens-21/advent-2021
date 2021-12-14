package day10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day10 {

    public static void main(String[] args) {
        try {
            List<String> linesOfInput = Files.readAllLines(Paths.get("src/day10/input.txt"));
            List<Error> errors = getErrorsFromLines(linesOfInput);
            System.out.println(sumCorruptedErrorValues(errors));
            System.out.println(getMedianAutoCompleteScore(linesOfInput));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getMedianAutoCompleteScore(List<String> lines) {
        List<Long> scores = lines.stream()
                                    .map(Day10::computeSequenceToFixLine)
                                    .filter(Objects::nonNull)
                                    .mapToLong(Day10::getAutoCompleteScore)
                                    .sorted()
                                    .boxed()
                                    .toList();

        return scores.get(scores.size() / 2);
    }

    private static long getAutoCompleteScore(List<Character> autoCompleteSequence) {
        long totalScore = 0;

        for (Character c : autoCompleteSequence) {
            totalScore *= 5;
            totalScore += getAutoCompleteCharValue(c);
        }

        return totalScore;
    }

    private static List<Character> computeSequenceToFixLine(String line) {
        StringBuilder currentLine = new StringBuilder(line);
        List<Character> charSequenceToFixLine = new ArrayList<>();

        Error e = parseErrorForLine(line);

        while (e != null) {
            if (e.getErrType() == EType.MISSING) {
                charSequenceToFixLine.add(e.getErrChar());
                currentLine.append(e.getErrChar());
                e = parseErrorForLine(currentLine.toString());
            } else {
                return null;
            }
        }

        return charSequenceToFixLine;
    }

    private static int sumCorruptedErrorValues(List<Error> errors) {
        return errors.stream().filter(e -> e.getErrType() == EType.CORRUPTED)
                              .mapToInt(e -> getErrorValue(e.getErrChar()))
                              .sum();
    }

    private static int getErrorValue(char corruptChar) {
        return switch (corruptChar) {
            case ')' -> 3;
            case ']' -> 57;
            case '}' -> 1197;
            case '>' -> 25137;

            default -> throw new IllegalStateException();
        };
    }

    private static int getAutoCompleteCharValue(char c) {
        return switch (c) {
            case ')' -> 1;
            case ']' -> 2;
            case '}' -> 3;
            case '>' -> 4;

            default -> throw new IllegalStateException();
        };
    }

    private static List<Error> getErrorsFromLines(List<String> lines) {
        List<Error> errors = new ArrayList<>();

        for (String l : lines) {
            errors.add(parseErrorForLine(l));
        }

        return errors;
    }

    private final static Set<Character> OPENINGS = Set.of('(', '[', '{', '<');

    private static Error parseErrorForLine(String line) {
        Stack<Character> targetStack = new Stack<>();
        int currentIndex = 0;
        targetStack.push(getClosing(line.charAt(currentIndex)));
        currentIndex += 1;

        while (currentIndex < line.length()) {
            if (!targetStack.isEmpty() && line.charAt(currentIndex) == targetStack.peek()) {
                targetStack.pop();
            } else {
                if (OPENINGS.contains(line.charAt(currentIndex))) {
                    targetStack.push(getClosing(line.charAt(currentIndex)));
                } else {
                    return new Error(EType.CORRUPTED, line.charAt(currentIndex));
                }
            }

            currentIndex += 1;
        }

        if (targetStack.isEmpty()) {
            return null;
        }

        return new Error(EType.MISSING, targetStack.peek());
    }

    private static char getClosing(char opening) {
        return switch (opening) {
            case '(' -> ')';
            case '{' -> '}';
            case '[' -> ']';
            case '<' -> '>';
            default -> throw new IllegalStateException("Unexpected value: " + opening);
        };
    }
}

enum EType {
    CORRUPTED,
    MISSING
}

class Error {
    EType errType;
    char errChar;

    public Error(EType e, char c) {
        errChar = c;
        errType = e;
    }

    public char getErrChar() {
        return errChar;
    }

    public EType getErrType() {
        return errType;
    }
}
