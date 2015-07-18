package io.mopar.core.msg;

/**
 * @author Hadyn Fitzgerald
 */
public interface MessageListener {
    void handle(Message message);
}
