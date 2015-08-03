package io.mopar.rs2.msg;

import io.mopar.core.msg.Message;
import io.mopar.rs2.net.packet.Packet;

/**
 * @author Hadyn Fitzgerald
 */
public interface MessageCodecContext {

    /**
     *
     * @param message
     * @return
     */
    Packet encode(Message message);
}
