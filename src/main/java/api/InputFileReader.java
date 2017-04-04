package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;


public interface InputFileReader<T> {
    /**
     * Reads a file from the path and pass its entries to a specified consumer.
     *
     * @param consumer the consumer to pass entries to
     * @param filePath path object containing real path of the file
     */
    void read(Consumer<T> consumer, Path filePath);

    /**
     * Reads a content from reader and pass its entries to a specified consumer.
     * Used for test purposes mostly
     *
     * @param consumer the consumer to pass entries to
     * @param reader   BufferedReader with the content to read and parse
     */
    void read(Consumer<Site> consumer, BufferedReader reader);
}
