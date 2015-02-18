package btill.terminal;

import com.google.gson.Gson;

import static btill.terminal.Response.Status.OK;

public class RequestMenuHandler implements Handler {
    private Menu menu;

    public RequestMenuHandler(Menu menu) {
        this.menu = menu;
    }

    @Override public void handle(Request request, Response response) {
        response.content(OK, menuSerializer().serialize(menu));
    }

    private MenuSerializer menuSerializer() {
        return new MenuSerializer();
    }

    public static class MenuDeserializer {
        public Menu deserialize(String content) {
            return new Gson().fromJson(content, Menu.class);
        }
    }

    public static class MenuSerializer {
        public String serialize(Menu menu) {
            return new Gson().toJson(menu);
        }
    }
}
