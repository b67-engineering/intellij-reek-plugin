package engineering.b67.intellij_linter_base;

public class Warning {
    private Integer line;
    private Integer column;
    private String description;
    private String url;
    private String type;

    public Warning(
        Integer line,
        Integer column,
        String type,
        String description
    ) {
        this.line = line;
        this.column = column;
        this.type = type;
        this.description = description;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getColumn() {
        return column;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // FIXME: Remove Reek reference
    public String getMessage() {
        return String.format("Reek: %s: %s.", this.type, this.description);
    }

    public String getFormattedMessage() {
        String message = getMessage();

        if (this.url != null) {
            message += String.format(" <a href=\"%s\">Documentation</a>.", this.url);
        }

        return message;
    }
}
