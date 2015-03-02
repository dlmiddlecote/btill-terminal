package btill.terminal.bluetooth;

import btill.terminal.Server;
import btill.terminal.values.BTMessage;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

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
            System.out.println("Waiting...");


            while (true) {
                StreamConnection connection = service.acceptAndOpen();
                InputStream incomingStream = connection.openInputStream();
                OutputStream out = connection.openOutputStream();
                // blocks until connection
                System.out.println("Connection Opened");
                //DataInputStream incomingStream = new DataInputStream(connection.openDataInputStream());
                System.out.println("Input Stream Opened");
                //String receivedString = incomingStream.readUTF();
                byte[] byteArray = new byte[1024];
                int bytes = 0;
                /* bytes = incomingStream.read(byteArray);
                String readMessageCount = new String(byteArray, 0, bytes);
                Integer readCountIn = Integer.parseInt(readMessageCount);
                String receivedString = new String();
                bytes = 0;
                int bytesTotal = 0;

                for (int i = 0; i < readCountIn.intValue(); i++) {
                    bytes = incomingStream.read(byteArray);
                    bytesTotal += bytes;
                    receivedString += new String(byteArray, 0, bytes);
                }
                */

                boolean read = true;
                int bytesTotal = 0;
                String receivedString = new String();
                while (read) {
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
