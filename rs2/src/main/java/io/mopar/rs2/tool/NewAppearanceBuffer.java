package io.mopar.rs2.tool;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by hadyn on 7/16/2015.
 */
public class NewAppearanceBuffer {

    public static void main(String... args) {
        ByteBuffer buf = ByteBuffer.allocate(5000);

        int[] styles = new int[] { 0, 10, 18, 26, 33, 36, 42 };
        int[] colors = new int[] { 2, 5, 8, 11, 14 };
        buf.put((byte)  0);
        buf.put((byte) -1);
        buf.put((byte) -1);

        buf.put((byte) 0);
        buf.put((byte) 0);
        buf.put((byte) 0);
        buf.put((byte) 0);

        buf.putShort((short) (0x100 | styles[2]));

        buf.put((byte) 0);

        buf.putShort((short) (0x100 | styles[3]));
        buf.putShort((short) (0x100 | styles[5]));
        buf.putShort((short) (0x100 | styles[0]));
        buf.putShort((short) (0x100 | styles[4]));
        buf.putShort((short) (0x100 | styles[1]));

        buf.put((byte) 0);

        for(int color : colors) {
            buf.put((byte) color);
        }

        buf.putShort((short) 1426);
        buf.putLong(1L);
        buf.put((byte) 3);

        buf.put((byte) 0);
        buf.put((byte) 0);
        buf.put((byte) 0);

        byte[] bytes = new byte[buf.position()];
        buf.flip();
        buf.get(bytes);

        System.out.println(Arrays.toString(bytes));
    }
}
