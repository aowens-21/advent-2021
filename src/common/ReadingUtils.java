package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadingUtils {
    public static Scanner makeScannerFromFile(File inFile) {
        try {
            return new Scanner(inFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + inFile.getAbsolutePath());
            e.printStackTrace();
        }

        return null;
    }
}
