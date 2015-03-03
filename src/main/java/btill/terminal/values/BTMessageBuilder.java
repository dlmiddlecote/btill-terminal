package btill.terminal.values;

import btill.terminal.Status;
import com.google.gson.Gson;

public class BTMessageBuilder {

    private String header = null;
    private byte[] body = null;

    public BTMessage build() {
        return new BTMessage(header, body);
    }

    public BTMessageBuilder(byte[] data) {
        BTMessage message = new Gson().fromJson(new String(data, 0, data.length), BTMessage.class);
        this.header = message.getHeader();
        this.body = message.getBody();
    }

    public BTMessageBuilder(Status status) {
        this.header = status.toString();
    }

    public BTMessageBuilder(Status status, Object obj) {
        this.header = status.toString();
        String json = new Gson().toJson(obj, obj.getClass());
        this.body = json.getBytes();
    }
}