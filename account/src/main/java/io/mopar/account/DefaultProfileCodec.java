package io.mopar.account;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class DefaultProfileCodec implements ProfileCodec {

    private static final int FILE_MAGIC = 0x4d4f7052;

    private static final int chk_EOF    = 0x00;
    private static final int chk_COORD  = 0x01;
    private static final int chk_INV    = 0x02;

    @Override
    public Profile decode(byte[] bytes) throws MalformedProfileException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);

        int magic = bb.getInt();
        if(magic != FILE_MAGIC) {
            throw new MalformedProfileException("Unexpected file magic number");
        }

        int len = bb.getShort() & 0xff;

        // Read the header bytes
        byte[] headerBytes = new byte[len];
        bb.get(headerBytes);

        ByteBuffer hbb = ByteBuffer.wrap(headerBytes);

        int format = hbb.get() & 0xff;
        if(format < 1 || format > 1) {
            throw new MalformedProfileException("Unsupported profile format version");
        }

        // Header
        // -----------------------------
        // uid  : int64 : format 1
        //
        Profile profile = new Profile();
        profile.setUid(hbb.getLong());

        int chk;
        while((chk = bb.get()) != chk_EOF) {
            switch (chk) {
                case chk_COORD: {
                    int hash = bb.getInt();
                    profile.setX(hash >> 14 & 0x3fff);
                    profile.setY(hash & 0x3fff);
                    profile.setPlane(hash >> 28 & 0x3);
                }
                break;

                case chk_INV: {
                    InventoryModel inventory = new InventoryModel();
                    inventory.setId(bb.getShort() & 0xffff);

                    int length = bb.getShort() & 0xffff;
                    while(length-- > 0) {
                        ItemModel item = new ItemModel();
                        item.setId(bb.getShort() & 0xffff);
                        item.setSlot(getSmart(bb));

                        int amount = bb.get() & 0xff;
                        if(amount == 255) {
                            item.setAmount(bb.getInt());
                        } else {
                            item.setAmount(amount);
                        }

                        inventory.addItem(item);
                    }
                    profile.addInventory(inventory);
                }
            }
        }

        return profile;
    }

    @Override
    public byte[] encode(Profile profile) throws MalformedProfileException {
        int length = 0;

        // File header
        // ---------------------
        // magic        : int32
        // headerlen    : int16
        length += 4 + 2;

        // Header
        // --------------------
        // format       : int8
        // uid          : int64
        int headerLength = 1 + 8;

        // Header
        length += headerLength;

        // Coordinate chunk
        length += 1 + 4;

        List<InventoryModel> inventories = profile.getInventories();
        for(InventoryModel inventory : inventories) {

            // id       : int16
            // slot     : int16
            length += 1 + 4;

            // id       : int16
            // slot     : smart
            // amount   : <variable>
            List<ItemModel> items = inventory.getItems();
            for(ItemModel model : items) {
                length += model.getSlot() < 128 ? 1 : 2;
                if(model.getAmount() >= 255) length += 5;
                else                         length += 1;
            }
            length += 2 * items.size();
        }

        // EOF chunk
        length += 1;

        byte[] bytes = new byte[length];

        ByteBuffer bb = ByteBuffer.wrap(bytes);

        // Put the file header
        bb.putInt(FILE_MAGIC);
        bb.putShort((short) headerLength);

        // Put the format
        bb.put((byte) 1);

        // Put the profile uid
        bb.putLong(profile.getUid());

        // Put the coord chunk
        bb.put((byte) chk_COORD);
        bb.putInt((profile.getPlane() & 0x3) << 28 | (profile.getX() & 0x3fff) << 14 | (profile.getY() & 0x3fff));

        // Put the inventory chunks
        for(InventoryModel inventory : profile.getInventories()) {
            bb.put((byte) chk_INV);

            List<ItemModel> items = inventory.getItems();
            bb.putShort((short) inventory.getId());
            bb.putShort((short) items.size());

            for(ItemModel item : items) {
                bb.putShort((short) item.getId());
                writeSmart(bb, item.getSlot());
                if(item.getAmount() >= 255) {
                    bb.put((byte) 255);
                    bb.putInt(item.getAmount());
                } else {
                    bb.put((byte) item.getAmount());
                }
            }
        }

        // Put the EOF chunk
        bb.put((byte) chk_EOF);

        return bytes;
    }

    /**
     *
     * @param bb
     * @return
     */
    private int getSmart(ByteBuffer bb) {
        int i = bb.get(bb.position()) & 0xff;
        return i < 128 ? bb.get() & 0xff : (bb.getShort() & 0xffff) - 0x8000;
    }

    /**
     * Writes a smart.
     *
     * @param i The integer value.
     */
    private void writeSmart(ByteBuffer buf, int i) {
        if(i >= -128 && i < 128) {
            buf.put((byte) i);
        } else if(i >= 128 && i <= 32767) {
            buf.putShort((short) (0x8000 | i));
        } else {
            throw new IllegalStateException("Bad value");
        }
    }
}
