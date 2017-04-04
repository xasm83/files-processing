import org.junit.Test;

public class CsvFileReaderTest extends AbstractInputFileReaderTest {

    @Test
    public void testCsvParsedCorrectly() {
        String fileContent = "id,name,is mobile,score\n" +
                "12000,example.com/csv1,true,454";
        testJsonParsedCorrectly(new CsvFileReader(), fileContent);
    }

    @Test(expected = NumberFormatException.class)
    public void testCsvReaderThrowsExceptionOnIncorrectFileFormat() {
        String fileContent = "id,name,is mobile,score\n" +
                "12000,example.com/csv1,true,__textInsteadOfScore__";
        testReaderThrowsExceptionOnIncorrectFileFormat(new CsvFileReader(), fileContent
        );
    }
}
