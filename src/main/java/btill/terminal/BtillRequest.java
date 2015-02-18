package btill.terminal;

public class BtillRequest implements Request {
    private final String header;
    private final String body;

    public BtillRequest(String header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public Command command() {
        return Command.toCommand(header);
    }

    @Override
    public String body() {
        return body;
    }
}
