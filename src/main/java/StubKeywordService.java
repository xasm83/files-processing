import api.KeywordService;
import api.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubKeywordService implements KeywordService {
    public String resolveKeywords(Object site) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if (!(site instanceof Site)) {
            throw new RuntimeException("Invalid entity type was passed as a parameter.");
        }
        char character = ((Site) site).getName().charAt(7);
        switch (character) {
            case '1':
                return "example1, extra1";
            case '2':
                return "example2, extra2";
            case '3':
                return "example3, extra3";
            default:
                return "keyword1, keyword2, defaultKeyword";
        }
    }
}
