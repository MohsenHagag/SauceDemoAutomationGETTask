package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public final class CsvDataReader {

    private CsvDataReader() {
    }


    public static List<String[]> readCsv(String classpathResource) {
        List<String[]> rows = new ArrayList<>();
        try (InputStream is = CsvDataReader.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new RuntimeException("CSV resource not found: " + classpathResource);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                boolean first = true;
                while ((line = reader.readLine()) != null) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    if (line.isBlank()) {
                        continue;
                    }
                    rows.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV resource: " + classpathResource, e);
        }
        return rows;
    }


    private static String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (insideQuotes) {
                if (c == '"') {
                    boolean nextIsQuote = i + 1 < line.length() && line.charAt(i + 1) == '"';
                    if (nextIsQuote) {
                        current.append('"');
                        i++;
                    } else {
                        insideQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    insideQuotes = true;
                } else if (c == ',') {
                    fields.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}