package btill.terminal;

import btill.terminal.values.BTMessage;
import com.google.gson.Gson;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BtillServerTest {
    private static final String SERVICE_URI = "someUri";
    private final Controller controller = new DummyController();
    private final BtillServer server = new BtillServer(SERVICE_URI);


    @Test
    public void canListenForConnections() {
        server.use(new DummyController());
        server.listenForConnectionsOn(dummyService());
    }

    @Test(expected=BtillServer.BluetoothConnectionException.class)
    public void errorThrownWhenNoBluetoothConnection() {
        server.start();
    }

    private DummyStreamConnectionNotifier dummyService() {
        return new DummyStreamConnectionNotifier();
    }

    private class DummyController extends Controller {
        public DummyController() {
            super(null, null);
        }

        public BTMessage processRequest(Command command, byte[] content) {
            return exampleMessage();
        }
    }


    private class DummyStreamConnectionNotifier implements StreamConnectionNotifier {
        int connectionsRequested = 0;
        private boolean closed = false;

        @Override
        public StreamConnection acceptAndOpen() throws IOException {
            if (connectionsRequested++ > 1) {
                return null;
            }
            return new DummyConnection();
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public boolean wasClosed() {
            return closed;
        }
    }


    private class DummyConnection implements StreamConnection {
        private boolean wasClosed = false;

        @Override
        public InputStream openInputStream() throws IOException {
            return new ByteArrayInputStream(asJsonString(exampleMessage()).getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {

                }
            };
        }

        @Override
        public DataInputStream openDataInputStream() throws IOException {
            throw new NotImplementedException();
        }

        @Override
        public DataOutputStream openDataOutputStream() throws IOException {
            throw new NotImplementedException();
        }

        @Override
        public void close() throws IOException {
            wasClosed = true;
        }
    }

    private String asJsonString(BTMessage msg) {
        return new Gson().toJson(msg);
    }

    private BTMessage exampleMessage() {
        return (new BTMessage(Command.REQUEST_MENU.toString(), "".getBytes()));
    }
}