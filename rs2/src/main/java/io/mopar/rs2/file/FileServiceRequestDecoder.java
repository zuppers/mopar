package io.mopar.rs2.file;

import io.mopar.rs2.msg.file.FileRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class FileServiceRequestDecoder extends ByteToMessageDecoder {

    public static final int NORMAL_REQUEST = 0;
    public static final int PRIORITY_REQUEST = 1;
    public static final int OFFLINE_STATUS = 2;
    public static final int ONLINE_STATUS = 3;
    public static final int UNKNOWN = 6;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        for(;;) {
            if (!buf.isReadable(4)) {
                return;
            }

            int opcode = buf.readUnsignedByte();

            switch (opcode) {
                case NORMAL_REQUEST:
                case PRIORITY_REQUEST:
                    int volumeId = buf.readUnsignedByte();
                    int fileId = buf.readUnsignedShort();
                    out.add(new FileRequestMessage(volumeId, fileId, opcode == PRIORITY_REQUEST));
                    break;

                case OFFLINE_STATUS:
                case ONLINE_STATUS:
                case UNKNOWN:
                    buf.skipBytes(3);
                    break;

                default:
                    throw new MalformedFileRequestException("Unrecognized opcode " + opcode);
            }
        }
    }
}