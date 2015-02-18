package btill.terminal;

import com.google.gson.Gson;

import static btill.terminal.Response.Status.OK;

public class MakeOrderHandler implements Handler {
    private Till till;

    public MakeOrderHandler(Till till) {
        this.till = till;
    }

    @Override
    public void handle(Request request, Response response) {
        MenuItem item = new MenuItemDeserializer().deserialize(request.body());
        Bill bill = till.createBillForAmount(item.cost());
        response.content(OK, new BillSerializer().serialize(bill));
    }

    public class MenuItemDeserializer {
        public MenuItem deserialize(String content) {
            return new Gson().fromJson(content, MenuItem.class);
        }
    }

    private class BillSerializer {
        public String serialize(Bill bill) {
            return new Gson().toJson(bill);
        }
    }
}
