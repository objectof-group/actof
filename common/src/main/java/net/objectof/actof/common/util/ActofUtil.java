package net.objectof.actof.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;


public class ActofUtil {

    public static String readFile(File file) throws FileNotFoundException {
        return readFile(new FileInputStream(file));
    }

    public static String readFile(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\Z");
        String contents = scanner.next();
        scanner.close();
        return contents;
    }

    public static String readFile(String filename) throws FileNotFoundException {
        return readFile(new File(filename));
    }

}
