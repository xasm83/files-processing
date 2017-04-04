import api.FieldName;
import api.FileProcessingException;
import api.Site;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

public class JsonFileWriter implements Consumer<Site> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JsonGenerator generator;

    public JsonFileWriter(JsonGenerator generator) {
        this.generator = generator;

        //this is required to avoid extra spaces after object close token
        this.generator.setPrettyPrinter(new MinimalPrettyPrinter("\n"));
    }

    public void accept(Site site) {
        try {
            generator.writeStartObject();
            generator.writeStringField(FieldName.ID.getValue(), site.getId());
            generator.writeStringField(FieldName.NAME.getValue(), site.getName());
            generator.writeNumberField(FieldName.MOBILE.getValue(), site.isMobile() ? 1 : 0);
            generator.writeStringField(FieldName.KEYWORDS.getValue(), site.getKeywords());
            generator.writeNumberField(FieldName.SCORE.getValue(), site.getScore());
            generator.writeEndObject();
        } catch (IOException ioException) {
            logAndThrow(ioException);
        }
    }

    public void stopCurrentCollection() {
        try {
            generator.writeEndArray();
            generator.writeEndObject();
            generator.flush();
        } catch (IOException ioException) {
            logAndThrow(ioException);
        }
    }

    public void startCollection(String collectionId) {
        try {
            generator.writeStartObject();
            generator.writeStringField(FieldName.COLLECTION_ID.getValue(), collectionId);
            generator.writeArrayFieldStart(FieldName.SITES.getValue());
        } catch (IOException ioException) {
            logAndThrow(ioException);
        }
    }

    private void logAndThrow(Exception exception) {
        String message = "Failed to write to output file.";
        logger.error(message, exception);
        throw new FileProcessingException(message, exception);
    }
}
