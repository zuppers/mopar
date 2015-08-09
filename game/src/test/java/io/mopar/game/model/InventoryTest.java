package io.mopar.game.model;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by hadyn on 7/20/2015.
 */
public class InventoryTest {

    @Test
    public void testAddNonStack() throws Exception {
        Inventory inv = new Inventory(50);
        inv.add(555, 20, false);
        assertEquals(20, inv.count(555));
        for(int slot = 0; slot < 20; slot++) {
            assertNotNull(inv.get(slot));
        }
    }

    @Test
    public void testAddStack() throws Exception {
        Inventory inv = new Inventory(50);
        inv.add(555, 1, true);
        inv.add(555, 2, true);
        assertEquals(3, inv.get(0).getAmount());
    }

    @Test
    public void testSet() throws Exception {
        Inventory inv = new Inventory(50);
        Item replace = new Item(20);
        inv.set(50, replace);
        assertEquals(replace, inv.set(50, new Item(5)));
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