package btill.terminal;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestBtillTerminal {

    private BtillTerminal terminal;
    private FakeServer server;

    @Before public void startServer() {
        terminal = new BtillTerminal();
        server = fakeServer();
        terminal.startUsing(server, fakeTill());
    }

    @Test public void startingTerminalStartsServer() {
        assertThat(server.wasStarted(), is(true));
    }

    @Test
    public void closingTerminalClosesServer() throws Exception {
        terminal.close();

        assertThat(server.wasClosed(), is(true));
    }

    private FakeServer fakeServer() {
        return new FakeServer();
    }

    private Till fakeTill() {
        return new FakeTill();
    }

    private class FakeServer implements Server {
        public boolean serverStarted = false;
        public boolean serverClosed = false;

        @Override
        public void start() {
            serverStarted = true;
        }

        @Override
        public void use(Controller controller) {

        }

        @Override
        public void close() throws Exception {
            serverClosed = true;
        }

        public boolean wasClosed() {
            return serverClosed;
        }

        public boolean wasStarted() {
            return serverStarted;
        }
    }
}


