import api.InputFileReader;
import api.KeywordService;
import api.Site;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FileProcessorTest {
    private KeywordService keywordService;
    private JsonFileReader fileReader;
    private JsonFileWriter jsonFileWriter;

    @Before
    public void initMocks() {
        keywordService = mock(KeywordService.class);
        fileReader = mock(JsonFileReader.class);
        jsonFileWriter = mock(JsonFileWriter.class);

    }

    @Test
    public void testFileProcessorProcessesCsvFiles() {
        String csvPath = "test.csv";
        String fileContent = "id,name,is mobile,score\n" +
                "12000,example.com/csv1,true,454";
        testFileProcessorProcessesFile(new CsvFileReader(), csvPath, fileContent);
    }

    @Test
    public void testFileProcessorProcessesJsonFiles() {
        String fileContent = "[{\"site_id\": \"12000\", \"name\": \"example.com/csv1\", \"mobile\": 1, \"score\": 454}]";
        String jsonPath = "test.json";
        testFileProcessorProcessesFile(new JsonFileReader(), jsonPath, fileContent);
    }


    private void testFileProcessorProcessesFile(InputFileReader<Site> reader, String pathString, String fileContent) {
        String keywords = "keywords";
        Site site = new Site("12000", "example.com/csv1", true, new BigDecimal("454"));
        when(keywordService.resolveKeywords(site)).thenReturn(keywords);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent.getBytes())));
        doAnswer(invocation -> {
            reader.read((Consumer<Site>) invocation.getArguments()[0], bufferedReader);
            return null;
        }).when(fileReader).read(any(), any(Path.class));

        Stream<Path> pathStream = Arrays.stream(new Path[]{Paths.get(pathString)});
        FilesProcessor filesProcessor = new FilesProcessor(
                keywordService,
                fileReader,
                fileReader,
                jsonFileWriter);
        filesProcessor.processFiles(pathStream);

        ArgumentCaptor<Site> siteCaptor = ArgumentCaptor.forClass(Site.class);
        verify(keywordService).resolveKeywords(siteCaptor.capture());
        assertEquals(site.getId(), siteCaptor.getValue().getId());
        verify(jsonFileWriter).startCollection(pathString);
        verify(fileReader).read(any(), any(Path.class));
        site.setKeywords(keywords);
        verify(jsonFileWriter).accept(site);
        verify(jsonFileWriter).stopCurrentCollection();

    }

    @Test
    public void testFileProcessorIgnoresUnknownExtension() {
        Stream<Path> pathStream = Arrays.stream(new Path[]{Paths.get("test.unsupported")});
        FilesProcessor filesProcessor = new FilesProcessor(
                keywordService,
                fileReader,
                fileReader,
                jsonFileWriter);
        filesProcessor.processFiles(pathStream);
        verifyZeroInteractions(jsonFileWriter, fileReader);
    }
}
