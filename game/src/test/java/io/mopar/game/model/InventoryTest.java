package io.mopar.game.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hadyn on 7/20/2015.
 */
public class InventoryTest {

    @Test
    public void testAdd() throws Exception {
        Inventory inv = new Inventory();
        inv.add(555, 20, false);
        assertEquals(20, inv.count(555));
        for(int slot = 0; slot < 20; slot++) {
            assertNotNull(inv.get(slot));
        }

        inv.add(555, 2, true);
        assertEquals(3, inv.get(0).getAmount());
    }

    @Test
    public void testSet() throws Exception {
        Inventory inv = new Inventory();
    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {

    }

    @Test
    public void testRemove1() throws Exception {

    }

    @Test
    public void testSlotOf() throws Exception {

    }

    @Test
    public void testFreeSlot() throws Exception {

    }

    @Test
    public void testFreeSlot1() throws Exception {

    }

    @Test
    public void testContains() throws Exception {

    }

    @Test
    public void testCount() throws Exception {

    }

    @Test
    public void testClear() throws Exception {

    }
}