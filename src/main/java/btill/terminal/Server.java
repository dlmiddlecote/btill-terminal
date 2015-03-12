package btill.terminal;

public interface Server extends AutoCloseable {
    void start();

    void use(Controller controller);


}
