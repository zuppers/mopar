package io.mopar.core;

/**
 * Created by hadyn on 7/25/2015.
 */
public class Base37 {

    private static char[] alphabet = "_abcdefghijklmnopqrstuvwxyz01234567890".toCharArray();
    private static byte[] table = new byte[256];

    /**
     *
     * @param str the string to encode. This is limited to twelve characters.
     * @return
     */
    public static long encode(String str) {
        str = str.toLowerCase();

        long l = 0L;
        for(int i = 0; i < str.length() && i < 12; i++) {
            l *= 37L;
            l += table[str.charAt(i)];
        }

        // Trim leading whitespace
        while((l % 37L) == 0L && l != 0L) {
            l /= 37L;
        }

        return l;
    }


    /**
     *
     * @param l
     * @return
     */
    public static String decode(long l) {
        if(l < 0L || l > 6582952005840035281L) {
            return null;
        }

        if(l == 0L) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        while(0L != l) {
            long prev = l;
            l /= 37L;
            builder.append(alphabet[(int) (prev - (l * 37L))]);
        }

        return builder.reverse().toString();
    }

    static {
        for(int i = 0; i < alphabet.length; i++) {
            table[alphabet[i]] = (byte) i;
        }
    }
}
