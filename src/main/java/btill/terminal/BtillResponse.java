package btill.terminal;

public class BtillResponse implements Response {
    private String body;
    private Status status;

    @Override public void status(Status status) {
        this.status = status;
    }

    @Override public void content(Status status, String body) {
        this.status = status;
        this.body = body;
    }

    public String status() {
        return status.toString();
    }

    public String body() {
        return body;
    }
}
