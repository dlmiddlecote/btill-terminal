package btill.terminal;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BtillServer implements Server {

    private Controller controller;
    private String serviceUri;
    private StreamConnectionNotifier service;

    public BtillServer(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    @Override
    public void start() {
        try {
            service = (StreamConnectionNotifier) Connector.open(serviceUri);
            while (true) {
                StreamConnection connection = service.acceptAndOpen(); // blocks until connection

                DataInputStream incomingStream = new DataInputStream(connection.openDataInputStream());

                BtillRequest request = createRequestFrom(incomingStream);

                DataOutputStream out = new DataOutputStream(connection.openOutputStream());

                BtillResponse response = new BtillResponse();

                controller.handle(request, response);

                out.writeBytes(response.status());
                out.writeBytes(":");
                out.writeBytes(response.body());

            }
        } catch (IOException e) {
            throw new BluetoothConnectionException(e);
        }
    }

    private BtillRequest createRequestFrom(DataInputStream in) throws IOException {
        String received = in.readUTF();
        int boundary = received.indexOf(":");
        String header = received.substring(0, boundary);
        String body = received.substring(boundary + 1);
        return new BtillRequest(header, body);
    }

    @Override
    public void use(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void close() throws Exception {
        if (service != null) {
            service.close();
        }
    }

    public class BluetoothConnectionException extends RuntimeException {
        public BluetoothConnectionException(IOException e) {
            super("Bluetooth connection error", e);
        }
    }

}
