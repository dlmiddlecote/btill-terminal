package btill.terminal.values;

import com.google.gson.Gson;

/**
 * Created by ap4314 on 24/02/15.
 */
public class BTMessage {
    private String header;
    private String body;

    public BTMessage(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public BTMessage(String receivedString) {
        BTMessage message = new Gson().fromJson(receivedString, BTMessage.class);
        this.header = message.header;
        this.body = message.body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
