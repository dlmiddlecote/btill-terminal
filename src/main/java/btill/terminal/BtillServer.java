package btill.terminal;

import btill.terminal.values.BTMessage;
import btill.terminal.values.BTMessageBuilder;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

import static btill.terminal.Command.toCommand;


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
            System.out.println("Waiting...");
            while (true) {
                StreamConnection connection = service.acceptAndOpen(); // blocks until connection
                InputStream incomingStream = connection.openDataInputStream();
                OutputStream out = connection.openOutputStream();
                System.out.println("Connection opened.");
                byte[] byteArray = new byte[1024];
                int bytes = 0;
                boolean read = true;
                int bytesTotal = 0;
                String receivedString = new String();
                while(read) {
                    bytes = incomingStream.read(byteArray);
                    bytesTotal += bytes;
                    receivedString += new String(byteArray, 0, bytes);
                    if (bytes != 990) {
                        read = false;
                    }
                }

                System.out.println("Read " + bytesTotal + " bytes, Received string: " + receivedString);
                BTMessage incomingMessage = new BTMessageBuilder(receivedString.getBytes()).build();

                BTMessage responseMessage = controller.processRequest(toCommand(incomingMessage.getHeader()), incomingMessage.getBody());
                System.out.println("Length: " + responseMessage.getBytes().length);

                byte[] messageBytes = responseMessage.getBytes();
                int remaining = messageBytes.length;
                for (int i = 0; i <= (messageBytes.length / 990); i++) {
                    out.write(messageBytes, i * 990, Math.min(990, remaining));
                    System.out.println("Written: " + new String(messageBytes, i * 990, Math.min(990, remaining)));
                    out.flush();
                    remaining -= 990;
                }

                System.out.println("Written to phone");
                incomingStream.close();
                out.close();
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
