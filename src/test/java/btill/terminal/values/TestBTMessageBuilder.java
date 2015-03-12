package btill.terminal.values;

import btill.terminal.Command;
import btill.terminal.Status;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class TestBTMessageBuilder {

    @Test
    public void testBuildHeaderOnly() throws Exception {
        BTMessageBuilder builder = new BTMessageBuilder("OK");
        BTMessage message = builder.build();
        assertEquals(Status.OK.toString(), message.getHeader());
        assertNull(message.getBody());
    }

    @Test
    public void testBuildMenu() throws Exception {
        ArrayList<MenuItem> testList = new ArrayList<MenuItem>();
        testList.add(new MenuItem("Coke", new GBP(150), "Drinks"));
        Menu testMenu = new Menu(testList);
        BTMessage message = new BTMessageBuilder(testMenu).build();
        assertEquals(Command.MAKE_ORDER.toString(), message.getHeader());
        assertNotNull(message.getBody());
    }
}
