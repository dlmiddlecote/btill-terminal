package btill.terminal.bluetooth;

import btill.terminal.Till;
import btill.terminal.values.*;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos.Payment;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static btill.terminal.bluetooth.Status.NOT_FOUND;
import static btill.terminal.bluetooth.Status.OK;

public class Controller {
    private final Menu menu;
    private final Till till;
    private GBP amount;

    public Controller(Menu menu, Till till) {
        this.menu = menu;
        this.till = till;
    }

    public BTMessage processRequest(Command command, byte[] content) {
        Menu newMenu = null;
        switch (command) {
            case REQUEST_MENU: {
                BTMessage message =  new BTMessageBuilder(OK, menu).build();
                //System.out.println("Read Count: " + message.getReadCount());
                return message;
            }
            case MAKE_ORDER: {
                newMenu = deserializeMenu(content);
                Bill bill = till.createBillForAmount(menu);
                BTMessage message = new BTMessageBuilder(OK, bill).build();
                return message;
            }
            case SETTLE_BILL: {
                SignedBill signedBill = deserializeSignedBill(content);
                //System.out.println("Amount: " + amount.toString());
                Future<Receipt> receiptFuture = till.settleBillUsing(signedBill);
                Receipt receipt = null;
                try {
                    receipt = receiptFuture.get();
                } catch (InterruptedException e) {
                    System.err.println("\nReceipt retrieval interrupted"); // TODO CHANGE TO LOGGING FORMAT
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    System.err.println("\nReceipt retrieval exception interrupted!"); // TODO CHANGE TO LOGGING FORMAT
                    e.printStackTrace();
                }
                return new BTMessageBuilder(OK, receipt).build();
            }
            default: {
                return new BTMessageBuilder(NOT_FOUND).build();
            }
        }

    }

    private Menu deserializeMenu(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), Menu.class);
    }

    private SignedBill deserializeSignedBill(byte[] content) {
        return new Gson().fromJson(new String(content, 0, content.length), SignedBill.class);
    }
}
