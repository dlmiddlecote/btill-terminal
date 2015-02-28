package btill.terminal;

import org.junit.Test;

import static btill.terminal.Command.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestCommandCreation {

    private static final String AN_INVALID_COMMAND_STRING = "invalid";

    private final String request_menu = "request_menu";
    private final String make_order = "make_order";
    private final String settle_bill = "settle_bill";


    @Test(expected = InvalidCommand.class)
    public void anInvalidCommandThrowsException() {
        Command.toCommand(AN_INVALID_COMMAND_STRING);
    }

    @Test
    public void stringInputIsRequestMenu() {
        assertThat(Command.toCommand(request_menu), is(REQUEST_MENU));
    }

    @Test
    public void stringInputIsMakeOrder() {
        assertThat(Command.toCommand(make_order), is(MAKE_ORDER));
    }

    @Test
    public void stringInputIsSettleBill() {
        assertThat(Command.toCommand(settle_bill), is(SETTLE_BILL));
    }
}
