package enums;

public enum HttpStatus {
    OK(200, "OK"),
    INTERNAL_SERVER(500, "Internal Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() { return this.code; }
    public String message() { return this.message; }
}
