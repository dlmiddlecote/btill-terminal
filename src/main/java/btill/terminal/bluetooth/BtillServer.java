package btill.terminal.bluetooth;

import btill.terminal.Server;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static btill.terminal.bluetooth.Command.toCommand;


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

                String received = incomingStream.readUTF();
                int boundary = received.indexOf(":");
                String header = received.substring(0, boundary);
                String body = received.substring(boundary + 1);

                DataOutputStream out = new DataOutputStream(connection.openOutputStream());

                BtillResponse response = controller.processRequest(toCommand(header), body);

                out.writeBytes(response.status());
                out.writeBytes(":");
                out.writeBytes(response.body());
                connection.close();

            }
        } catch (IOException e) {
            throw new BluetoothConnectionException(e);
        }
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
