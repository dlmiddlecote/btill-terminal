package btill.terminal.values;

import btill.terminal.Status;
import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBTMessage {

    BTMessage testMessage = new BTMessage(Status.OK.toString(), new String("This is a body").getBytes());

    @Test
    public void testGetHeader() throws Exception {
        assertEquals(Status.OK.toString(), testMessage.getHeader());
    }

    @Test
    public void testGetBodyString() throws Exception {
        assertEquals("This is a body", testMessage.getBodyString());
    }

    @Test
    public void testGetBytes() throws Exception {
        BTMessage newMessage = new Gson().fromJson(new String(testMessage.getBytes()), BTMessage.class);
        assertEquals(Status.OK.toString(), newMessage.getHeader());
    }
}
