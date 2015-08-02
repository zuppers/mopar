package io.mopar.game.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObjectConfig {

    /**
     *
     */
    private static GameObjectConfig[] configs;

    /**
     *
     */
    private int width = 1;

    /**
     *
     */
    private int height = 1;

    /**
     *
     */
    private boolean solid = true;

    /**
     *
     */
    private boolean impenetrable = true;

    /**
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     *
     * @return
     */
    public boolean isImpenetrable() {
        return impenetrable;
    }

    /**
     *
     * @param ois
     */
    public static void init(InputStream ois) {
        try {
            DataInputStream is = new DataInputStream(new GZIPInputStream(ois));
            is.readInt();
            int count = is.readUnsignedShort();
            configs = new GameObjectConfig[count];

            int id;
            while ((id = is.readUnsignedShort()) != 65535) {
                GameObjectConfig config = configs[id] = new GameObjectConfig();

                int chk;
                while ((chk = is.readByte()) != 0) {
                    if (chk == 3) {
                        config.width = is.readUnsignedByte();
                    }

                    if (chk == 4) {
                        config.height = is.readUnsignedByte();
                    }

                    if(chk == 5) {
                        config.solid = false;
                        config.impenetrable = false;
                    }

                    if(chk == 6) {
                        config.impenetrable = false;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public static GameObjectConfig forId(int id) {
        return configs[id];
    }
}
