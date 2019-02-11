package hello.api.gateway.models;

public enum ErrorCodes {
    ERROR_401("{\"error\":\" asd\"}"),
    SPRING("Cool season"),
    SUMMER("Hot season"),
    FALL("Cool season");

    private String error;

    ErrorCodes(String error) {
        this.error = error;
    }

    public String error() {
        return error;
    }
}
