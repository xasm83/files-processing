package api;

public interface KeywordService {

    /**
     * Resolves a list of keywords associated with a site.
     *
     * @param site
     * @return a comma delimited string or an empty string if there are no keywords associated with the site.
     */
    String resolveKeywords(Object site);

}
