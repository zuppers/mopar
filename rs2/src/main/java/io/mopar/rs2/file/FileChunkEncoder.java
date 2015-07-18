package io.mopar.rs2.file;

import io.mopar.file.FileChunk;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Hadyn Fitzgerald
 *
 * Encodes the data for a file chunk.
 */
public class FileChunkEncoder extends MessageToByteEncoder<FileChunk> {

    @Override
    protected void encode(ChannelHandlerContext ctx, FileChunk chunk, ByteBuf out) throws Exception {
        out.writeBytes(chunk.getBytes(), 0, chunk.getLength());
    }
}
