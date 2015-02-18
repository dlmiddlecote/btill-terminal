package btill.terminal;

import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos.Payment;

import static btill.terminal.Response.Status.OK;

public class SettleBillHandler implements Handler {

    private Till till;

    public SettleBillHandler(Till till) {
        this.till = till;
    }

    @Override
    public void handle(Request request, Response response) {
        Payment payment = new PaymentDeserializer().deserialize(request.body());
        Receipt receipt = till.settleBillUsing(payment);
        response.content(OK, new ReceiptSerializer().serialize(receipt));
    }


    public class PaymentDeserializer {
        public Payment deserialize(String content) {
            return new Gson().fromJson(content, Payment.class);
        }
    }

    private class ReceiptSerializer {
        public String serialize(Receipt receipt) {
            return new Gson().toJson(receipt);
        }

    }
}