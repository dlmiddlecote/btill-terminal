package btill.terminal;

import btill.terminal.bluetooth.Controller;

public interface Server extends AutoCloseable {
    void start();

    void use(Controller controller);

}

