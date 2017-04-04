import api.FileProcessingException;
import api.InputFileReader;
import api.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CsvFileReader implements InputFileReader<Site> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void read(Consumer<Site> consumer, Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            read(consumer, reader);
        } catch (IOException exception) {
            String message = "Unable to read CSV file. File path: " + filePath.toAbsolutePath();
            logger.error(message, exception);
            throw new FileProcessingException(message, exception);
        }
    }

    public void read(Consumer<Site> consumer, BufferedReader reader) {
        reader.lines().
                skip(1).
                map(this::parseLine).
                forEach(consumer);
    }

    private Site parseLine(String line) {
        String[] fields = line.split(",");
        return new Site(fields[0],
                fields[1],
                Boolean.valueOf(fields[2]),
                new BigDecimal(fields[3]));
    }
}
