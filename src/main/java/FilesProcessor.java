import api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FilesProcessor {
    private static final String EXTENSION_PATTERN = "\\.([^.]*)$";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private KeywordService keywordService;
    private InputFileReader<Site> jsonReader;
    private InputFileReader<Site> csvReader;
    private JsonFileWriter fileWriter;

    public FilesProcessor(KeywordService keywordService, InputFileReader<Site> jsonReader, InputFileReader<Site> csvReader, JsonFileWriter fileWriter) {
        this.keywordService = keywordService;
        this.jsonReader = jsonReader;
        this.csvReader = csvReader;
        this.fileWriter = fileWriter;
    }

    public void processFiles(Stream<Path> files) {
        files.forEach(path -> {
            try {
                Consumer<Site> keywordResolver = (site) -> {
                    String keywords = keywordService.resolveKeywords(site);
                    site.setKeywords(keywords);
                };
                Consumer<Site> keywordResolvingWriter = keywordResolver.andThen(fileWriter);

                String fileExtension = getFileExtension(path);
                InputFileReader<Site> fileReader;
                if (FileExtension.JSON.getValue().equals(fileExtension)) {
                    fileReader = jsonReader;
                } else if (FileExtension.CSV.getValue().equals(fileExtension)) {
                    fileReader = csvReader;
                } else {
                    logger.warn("Unsupported file extension under source root. Ignoring the file: {}", path.toAbsolutePath());
                    return;
                }
                fileWriter.startCollection(path.getFileName().toString());
                fileReader.read(keywordResolvingWriter, path);
                fileWriter.stopCurrentCollection();
            } catch (Exception exception) {
                String message = "Failed to process files.";
                logger.error(message, exception);
                throw new FileProcessingException(message, exception);
            }
        });
    }

    private String getFileExtension(Path path) {
        logger.info("Processing file path: {}", path.toAbsolutePath());
        Matcher matcher = Pattern.compile(EXTENSION_PATTERN).matcher(path.toString());
        if (matcher.find()) {
            String fileExtensionString = matcher.group(1).toLowerCase();
            logger.info("Matched the following file extension:{}", fileExtensionString);
            return fileExtensionString;
        } else {
            return "";
        }

    }
}
