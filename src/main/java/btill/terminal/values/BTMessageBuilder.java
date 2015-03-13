package btill.terminal.values;

import btill.terminal.Status;
import com.google.gson.Gson;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;

public class BTMessageBuilder {

    private String header = null;
    private byte[] body = null;

    public BTMessage build() { return new BTMessage(header, body); }

    public BTMessageBuilder(String header) {
        this.header = header;
    }

    public BTMessageBuilder(byte[] data) {
        BTMessage message = new Gson().fromJson(new String(data, 0, data.length), BTMessage.class);
        this.header = message.getHeader();
        this.body = message.getBody();
    }

    public BTMessageBuilder(Status status) {
        this.header = status.toString();
    }

    public BTMessageBuilder(Protos.Payment payment, GBP gbpAmount, Coin btcAmount) {
        this.header = "SETTLE_BILL";
        String json = new Gson().toJson(new SignedBill(payment, gbpAmount, btcAmount), SignedBill.class);
        this.body = json.getBytes();
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

}
