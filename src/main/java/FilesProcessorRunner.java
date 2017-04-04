import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FilesProcessorRunner {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(FilesProcessorRunner.class);
        int exitCode = 1;
        try {
            if (args.length != 2) {
                logger.error("Invalid parameters. Usage: java -jar xxx.jar sourceDir outputFile");
                exitCode = -1;
            }

            Path sourceDir = Paths.get(args[0]);
            Path outputFile = Paths.get(args[1]);
            logger.info("Got the following input parameters. Source dir:{}. Output file:{}",
                    sourceDir.toAbsolutePath(),
                    outputFile.toAbsolutePath());
            if (Files.exists(outputFile)) {
                logger.error("Output file exists. File path:{}", outputFile.toAbsolutePath());
                exitCode = -1;
            }

            try (Stream<Path> list = Files.list(sourceDir);
                 BufferedWriter writer = Files.newBufferedWriter(outputFile);
                 JsonGenerator generator = new JsonFactory().createGenerator(writer)) {
                FilesProcessor filesProcessor = new FilesProcessor(
                        new StubKeywordService(),
                        new JsonFileReader(),
                        new CsvFileReader(),
                        new JsonFileWriter(generator));
                filesProcessor.processFiles(list);
            }
        } catch (InvalidPathException pathException) {
            exitCode = -1;
            logger.error("Found an invalid path.", pathException);
        } catch (Exception exception) {
            exitCode = -1;
            logger.error("Fail to process files.", exception);

        }
        System.exit(exitCode);
    }
}
