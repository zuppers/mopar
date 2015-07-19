package io.mopar.game.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 *
 * Notes:
 *
 * Chat box overlay is toggleable
 */
public class InterfaceSet {

    public static final int WELCOME = 549;
    public static final int FIXED = 548;
    public static final int RESIZABLE = -1;

    public static final int FIXED_CHAT_BOX = 76;
    public static final int FIXED_CHAT_BOX_OVERLAY = 77;


    public static final int FIXED_HITPOINTS_ORB = 70;
    public static final int FIXED_PRAYER_ORB = 71;
    public static final int FIXED_ENERGY_ORB = 72;
    public static final int FIXED_SUMMONING_ORB = 73;

    private int rootId;
    private Map<Integer, Integer> slots = new HashMap<>();

    InterfaceSet(int rootId) {
        this.rootId = rootId;
    }

    int getRootId() {
        return rootId;
    }

    int getSlot(int type) {
        return slots.get(type);
    }

    /**
     *
     * @param mode
     * @return
     */
    public static InterfaceSet forMode(int mode) {
        InterfaceSet set = new InterfaceSet(FIXED);
        return set;
    }

    private static InterfaceSet createFixedInterfaceSet() {
        return new InterfaceSet(FIXED);
    }
}
