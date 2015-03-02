package btill.terminal.values;

import btill.terminal.Status;
import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos.Payment;

public class BTMessageBuilder {

    private String header = null;
    private byte[] body = null;

    public BTMessage build() {
        return new BTMessage(header, body);
    }

    // Do we need this?
    //    public BTMessageBuilder(String header) {
    //        this.header = header;
    //    }

    public BTMessageBuilder(byte[] data) {
        BTMessage message = new Gson().fromJson(new String(data, 0, data.length), BTMessage.class);
        this.header = message.getHeader();
        this.body = message.getBody();
    }
//
//        public BTMessageBuilder(String header, byte[] body) {
//            this.header = header;
//            this.body = body;
//        }
//
//        public BTMessageBuilder(String header, String body) {
//            this.header = header;
//            this.body = body.getBytes();
//        }

    public BTMessageBuilder(Status status, String body) {
        this.header = status.toString();
        this.body = body.getBytes();
    }

    public BTMessageBuilder(Status status) {
        this.header = status.toString();
    }

    public BTMessageBuilder(Payment payment) {
        this.header = "SETTLE_BILL";
        this.body = payment.toByteArray();
    }

    public BTMessageBuilder(Menu menu) {
        this.header = "MAKE_ORDER";
        String json = new Gson().toJson(menu, Menu.class);
        this.body = json.getBytes();
    }

    public BTMessageBuilder(Status status, Object obj) {
        this.header = status.toString();
        String json = new Gson().toJson(obj, obj.getClass());
        this.body = json.getBytes();
    }

    public BTMessageBuilder(Status status, Bill bill) {
        this.header = status.toString();
        String json = new Gson().toJson(bill, Bill.class);
        this.body = json.getBytes();
    }

    public BTMessageBuilder setHeader(String header) {
        this.header = header;
        return this;
    }

    public BTMessageBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public BTMessageBuilder setBody(String body) {
        this.body = body.getBytes();
        return this;
    }

}