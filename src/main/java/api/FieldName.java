package api;

public enum FieldName {
    NAME("name"),
    ID("id"),
    SITE_ID("site_id"),
    MOBILE("mobile"),
    SCORE("score"),
    KEYWORDS("keywords"),
    COLLECTION_ID("collectionId"),
    SITES("sites");

    private String value;

    FieldName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
