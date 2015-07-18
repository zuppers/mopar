package io.mopar.rs2.net;

import io.mopar.core.msg.Message;
import io.mopar.rs2.msg.MessageDispatcher;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class Session {

    /**
     * The channel.
     */
    private SocketChannel channel;

    /**
     * The message dispatcher.
     */
    private MessageDispatcher dispatcher;

    /**
     * Flag for if the session is closed.
     */
    private boolean closed;

    /**
     * The attachments.
     */
    private Map<Class<? extends Attachment>, Attachment> attachments = new HashMap<>();

    /**
     * Constructs a new {@link SocketChannel}
     *
     * @param channel The channel.
     */
    public Session(SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Sets the message dispatcher.
     *
     * @param dispatcher The dispatcher.
     */
    public void setDispatcher(MessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Appends an attachment.
     *
     * @param key The attachment class key.
     * @param attachment The attachment.
     * @param <T> The generic attachment type.
     */
    public <T extends Attachment> void attach(Class<T> key, T attachment) {
        attachments.put(key, attachment);
    }

    /**
     * Gets an attachment.
     *
     * @param key The attachment class key.
     * @param <T> The generic attachment type.
     * @return The attachment for the provided key.
     */
    public <T extends Attachment> T get(Class<T> key) {
        return (T) attachments.get(key);
    }

    /**
     * Gets the channel pipeline.
     *
     * @return The pipeline.
     */
    public ChannelPipeline pipeline() {
        return channel.pipeline();
    }

    /**
     * Dispatches a message.
     *
     * @param message The message to dispatch.
     */
    public void dispatch(Message message) {
        if(dispatcher != null) {
            dispatcher.dispatch(this, message);
        }
    }

    /**
     * Writes and flushes the message to the channel pipeline.
     *
     * @param message The message to write.
     * @return the channel future.
     */
    public ChannelFuture writeAndFlush(Object message) {
        return channel.writeAndFlush(message);
    }

    /**
     * Writes a message.
     *
     * @param message The message to write.
     * @return The channel future.
     */
    public ChannelFuture write(Object message) {
        return channel.write(message);
    }

    /**
     * Flushes the written messages to the channels pipeline.
     */
    public void flush() {
        channel.flush();
    }

    /**
     * Closes the session.
     */
    public void close() {
        synchronized (this) {
            if (!closed) {
                // Dispatch a message indicating that the session has been closed
                // TODO(sini): Should I attach a reason in the form of an exception?
                dispatch(new SessionClosedMessage());

                // Close the channel
                channel.close();

                // Mark the session as closed
                closed = true;
            }
        }
    }
}
