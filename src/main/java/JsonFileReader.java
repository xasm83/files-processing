import api.FieldName;
import api.FileProcessingException;
import api.InputFileReader;
import api.Site;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class JsonFileReader implements InputFileReader<Site> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void read(Consumer<Site> consumer, Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            read(consumer, reader);
        } catch (Exception exception) {
            String message = "Fail to read from file. File path: " + filePath.toAbsolutePath();
            logger.error(message, exception);
            throw new FileProcessingException(message, exception);
        }
    }

    public void read(Consumer<Site> consumer, BufferedReader reader) {
        try (JsonParser jsonParser = new JsonFactory().createParser(reader)) {
            JsonToken nextToken = jsonParser.nextToken();
            while (nextToken != null && !JsonToken.END_ARRAY.equals(nextToken)) {
                if (JsonToken.START_OBJECT.equals(nextToken)) {
                    Site site = new Site();
                    while (!JsonToken.END_OBJECT.equals(jsonParser.nextToken())) {
                        String fieldName = jsonParser.getCurrentName();
                        jsonParser.nextToken();

                        if (FieldName.SITE_ID.getValue().equals(fieldName)) {
                            site.setId(jsonParser.getText());
                        }

                        if (FieldName.NAME.getValue().equals(fieldName)) {
                            site.setName(jsonParser.getText());
                        }

                        if (FieldName.MOBILE.getValue().equals(fieldName)) {
                            site.setMobile(jsonParser.getIntValue() == 1);
                        }

                        if (FieldName.SCORE.getValue().equals(fieldName)) {
                            site.setScore(jsonParser.getDecimalValue());
                        }
                    }
                    consumer.accept(site);
                }
                nextToken = jsonParser.nextToken();
            }
        } catch (IllegalArgumentException exception) {
            String message = "Fail to parse file. Unsupported field name.";
            logger.error(message, exception);
            throw new FileProcessingException(message, exception);
        } catch (IOException exception) {
            String message = "Fail to read from file.";
            logger.error(message, exception);
            throw new FileProcessingException(message, exception);
        }
    }
}