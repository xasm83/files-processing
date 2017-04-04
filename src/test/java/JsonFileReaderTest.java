import api.FileProcessingException;
import org.junit.Test;

public class JsonFileReaderTest extends AbstractInputFileReaderTest {

    @Test
    public void testJsonParsedCorrectly() {
        String fileContent = "[{\"site_id\": \"12000\", \"name\": \"example.com/csv1\", \"mobile\": 1, \"score\": 454}]";
        testJsonParsedCorrectly(new JsonFileReader(), fileContent);
    }

    @Test(expected = FileProcessingException.class)
    public void testJsonReaderTrowsExceptionOnIncorrectFileFormat() {
        String fileContent = "[{\"site_id\": \"12000\", \"name\": \"example.com/csv1\", \"mobile\": 1, \"score\": _incorrectvalue_}]";
        testReaderThrowsExceptionOnIncorrectFileFormat(new JsonFileReader(), fileContent);
    }
}

