import api.InputFileReader;
import api.Site;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

abstract class AbstractInputFileReaderTest {
    private Site site;

    private BufferedReader getBufferedReader(String bufferString) {
        InputStream is = new ByteArrayInputStream(bufferString.getBytes());
        return new BufferedReader(new InputStreamReader(is));
    }

    protected void testJsonParsedCorrectly(InputFileReader<Site> inputFileReader, String fileContent) {
        site = null;
        inputFileReader.read(item -> {
            site = item;
        }, getBufferedReader(fileContent));
        assertEquals("12000", site.getId());
        assertEquals("example.com/csv1", site.getName());
        assertEquals(new BigDecimal("454"), site.getScore());
        assertEquals(true, site.isMobile());
    }


    protected void testReaderThrowsExceptionOnIncorrectFileFormat(InputFileReader<Site> inputFileReader, String fileContent) {
        site = null;
        inputFileReader.read(item -> {
            site = item;
        }, getBufferedReader(fileContent));
    }
}
