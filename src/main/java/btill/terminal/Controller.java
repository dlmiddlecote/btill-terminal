package btill.terminal;

import java.util.Map;
import java.util.TreeMap;

import static btill.terminal.Request.Command;
import static btill.terminal.Response.Status.NOT_FOUND;

public class Controller {
    private final Map<Command, Handler> handlers = new TreeMap<>();

    public void handle(Request request, Response response) {
        Command command = request.command();

        if (!handlers.containsKey(command)) {
            response.status(NOT_FOUND);
            return;
        }
        handlers.get(command).handle(request, response);
    }

    public void register(Command command, Handler handler) {
        handlers.put(command, handler);
    }
}
