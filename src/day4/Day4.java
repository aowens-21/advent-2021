package day4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) {
        try {
            System.out.println(computeFirstFinalBingoScore(new File("src/day4/input.txt")));
            System.out.println(computeLastFinalBingoScore(new File("src/day4/input.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int computeFirstFinalBingoScore(File inFile) throws IOException {
        Scanner gameScanner = new Scanner(inFile);
        int[] numsToCall = Arrays.stream(gameScanner.nextLine().split(",")).mapToInt((Integer::parseInt)).toArray();
        BingoManager manager = new BingoManager(gameScanner);

        for (int n : numsToCall) {
            int maybeFinalScore = manager.callBingoNumberAndHandleWin(n, false);

            if (maybeFinalScore != -1) {
                return maybeFinalScore;
            }
        }

        // nobody ever won, I don't think this happens
        return -1;
    }

    private static int computeLastFinalBingoScore(File inFile) throws IOException {
        Scanner gameScanner = new Scanner(inFile);
        int[] numsToCall = Arrays.stream(gameScanner.nextLine().split(",")).mapToInt((Integer::parseInt)).toArray();
        BingoManager manager = new BingoManager(gameScanner);

        int lastFinalScore = -1;

        for (int n : numsToCall) {
            int maybeFinalScore = manager.callBingoNumberAndHandleWin(n, true);

            if (maybeFinalScore != -1) {
                lastFinalScore = maybeFinalScore;
            }
        }

        return lastFinalScore;
    }
}

class BingoManager {
    private ArrayList<Board> boards = new ArrayList<>();

    public BingoManager(Scanner boardFileScanner) {
        while (boardFileScanner.hasNextLine()) {
            // skip newline in between boards
            boardFileScanner.nextLine();

            String[] boardStringArray = new String[Board.BOARD_SIZE];
            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                boardStringArray[i] = boardFileScanner.nextLine();
            }
            boards.add(new Board(boardStringArray));
        }
    }

    public int callBingoNumberAndHandleWin(int number, boolean useLast) {
        ArrayList<Integer> wonScores = new ArrayList<>();

        for (Board b : boards) {
            b.markSpaceIfPresent(number);

            if (b.getHasWon()) {
                // Someone won, do the computation to get score
                int unmarkedSum = b.getSumOfUnmarkedNumbers();
                wonScores.add(unmarkedSum * number);
            }
        }

        List<Board> notWonBoards = boards.stream().filter(b -> !b.getHasWon()).toList();
        boards = new ArrayList<>(notWonBoards);

        // nobody won, return -1
        if (wonScores.size() > 0) {
            if (useLast) {
                return wonScores.get(wonScores.size() - 1);
            } else {
                return wonScores.get(0);
            }
        }

        return -1;
    }
}

class Board {
    public static final int BOARD_SIZE = 5;
    private BoardSpace[][] boardArray = new BoardSpace[BOARD_SIZE][BOARD_SIZE];
    private boolean hasWon = false;

    public Board(String[] boardStringArray) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            String[] rowStringArray = boardStringArray[row].trim().split("\\s+");
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardArray[row][col] = new BoardSpace(Integer.parseInt(rowStringArray[col]));
            }
        }
    }

    public void markSpaceIfPresent(int bingoNumber) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (boardArray[row][col].getValue() == bingoNumber) {
                    boardArray[row][col].markSpace(true);
                    markBoardAsWinningIfNecessary();
                }
            }
        }
    }

    private void markBoardAsWinningIfNecessary() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            boolean columnHasWon = true;
            boolean rowHasWon = true;
            for (int col = 0; col < BOARD_SIZE && (columnHasWon || rowHasWon); col++) {
                if (!boardArray[row][col].getIsMarked()) {
                    columnHasWon = false;
                }
                if (!boardArray[col][row].getIsMarked()) {
                    rowHasWon = false;
                }
            }
            if (columnHasWon || rowHasWon) {
                hasWon = true;
                return;
            }
        }
    }

    public int getSumOfUnmarkedNumbers() {
        int sum = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (!boardArray[row][col].getIsMarked()) {
                    sum += boardArray[row][col].getValue();
                }
            }
        }

        return sum;
    }

    public boolean getHasWon() {
        return hasWon;
    }
}

class BoardSpace {
    private boolean isMarked = false;
    private final int value;

    public BoardSpace(int v) {
        this.value = v;
    }

    public void markSpace(boolean marked) {
        isMarked = marked;
    }

    public boolean getIsMarked() {
        return isMarked;
    }

    public int getValue() {
        return this.value;
    }
}