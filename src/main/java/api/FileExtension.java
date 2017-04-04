package api;

public enum FileExtension {
    JSON("json"), CSV("csv");
    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
