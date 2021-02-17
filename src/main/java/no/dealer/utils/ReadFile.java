package no.dealer.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReadFile {
    public static String readFileFromCode(String filePath) throws IOException {
        StringBuilder stringBuilder;
        try (BufferedReader bufferedReader = new BufferedReader(getFileFromPath(filePath))) {
            stringBuilder = new StringBuilder();
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

    public static String getFilePathFromConsole() throws IOException {
        System.out.println("Please enter your fileName with path e.g. 'deck/deck.txt' " );
        System.out.println("or just push Enter to generate a shuffled deck for you");
        System.out.print("(current directory is "+System.getProperty("user.dir")+") :");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        return bufferedReader.readLine();
    }

    private static InputStreamReader getFileFromPath(String filePath) {
        InputStream resourceAsStream = ReadFile.class.getClassLoader().getResourceAsStream(filePath);

        if (resourceAsStream == null){
            throw new IllegalArgumentException("File not found! "+ filePath);
        }else {
            return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        }
    }
}
