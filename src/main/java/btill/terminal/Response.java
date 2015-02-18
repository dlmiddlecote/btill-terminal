package btill.terminal;

public interface Response {
    void status(Status status);

    void content(Status status, String content);

    public enum Status {
        OK,
        NOT_FOUND
    }
}
