package btill.terminal;

import com.google.gson.Gson;

public class BillDeserializer {
    public Bill deserialize(String content) {
        return new Gson().fromJson(content, Bill.class);
    }
}
