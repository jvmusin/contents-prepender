package jvmusin.customaggregations;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    /**
     * Main method.
     *
     * <p>Reads the file provided in a first cmd argument,
     * prepends its content with its table of contents and prints it to `stdout`.
     */
    @SneakyThrows
    public static void main(String[] args) {
        try {
            ContentsExtractor extractor = new ContentsExtractor();
            ContentsRenderer renderer = new ContentsRenderer(4);
            ContentsValidator validator = new ContentsValidator();
            ContentsPrepender prepender = new ContentsPrepender(extractor, renderer, validator);
            Path file = Paths.get(args[0]);
            String text = Files.readString(file);
            System.out.println(prepender.prependWithContents(text));
        } catch (Exception e) {
            System.err.println("Unable to prepend the file with its content table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
