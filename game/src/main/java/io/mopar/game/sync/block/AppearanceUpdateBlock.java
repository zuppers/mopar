package io.mopar.game.sync.block;

import io.mopar.game.model.Appearance;
import io.mopar.game.model.Equipment;
import io.mopar.game.model.Inventory;
import io.mopar.game.model.Player;
import io.mopar.game.sync.UpdateBlock;

/**
 * @author Hadyn Fitzgerald
 */
public class AppearanceUpdateBlock extends UpdateBlock {

    /**
     * Creates a new {@link AppearanceUpdateBlock} from the provided player.
     *
     * @param player the player.
     * @return the created block.
     */
    public static AppearanceUpdateBlock create(Player player) {
        AppearanceUpdateBlock block = new AppearanceUpdateBlock();

        block.setName(player.getUid());
        block.setCombatLevel(3);

        if(player.hasInventory(Equipment.INVENTORY_ID)) {
            Inventory inventory = player.getInventory(Equipment.INVENTORY_ID);

            if(!inventory.isEmpty(Equipment.HEAD)) {
                block.setHelmetId(inventory.getId(Equipment.HEAD));
            }

            if(!inventory.isEmpty(Equipment.CAPE)) {
                block.setCapeId(inventory.getId(Equipment.CAPE));
            }

            if(!inventory.isEmpty(Equipment.SHIELD)) {
                block.setShieldId(inventory.getId(Equipment.SHIELD));
            }

            if(!inventory.isEmpty(Equipment.NECK)) {
                block.setNecklaceId(inventory.getId(Equipment.NECK));
            }

            if(!inventory.isEmpty(Equipment.HANDS)) {
                block.setGlovesId(inventory.getId(Equipment.HANDS));
            }

            if(!inventory.isEmpty(Equipment.WEAPON)) {
                block.setWeaponId(inventory.getId(Equipment.WEAPON));
            }

            if(!inventory.isEmpty(Equipment.BODY)) {
                block.setChestId(inventory.getId(Equipment.BODY));
            }

            if(!inventory.isEmpty(Equipment.LEGS)) {
                block.setPantsId(inventory.getId(Equipment.LEGS));
            }

            if(!inventory.isEmpty(Equipment.FEET)) {
                block.setShoesId(inventory.getId(Equipment.FEET));
            }
        }

        Appearance appearance = player.getAppearance();

        block.setMale(appearance.isMale());

        if(appearance.isVisible(Appearance.HEAD)) {
            block.setHeadStyle(appearance.getStyle(Appearance.HEAD));
        }

        if(appearance.isVisible(Appearance.BEARD)) {
            block.setBeardStyle(appearance.getStyle(Appearance.BEARD));
        }

        if(appearance.isVisible(Appearance.BODY)) {
            block.setBodyStyle(appearance.getStyle(Appearance.BODY));
        }

        if(appearance.isVisible(Appearance.ARMS)) {
            block.setArmsStyle(appearance.getStyle(Appearance.ARMS));
        }

        if(appearance.isVisible(Appearance.HANDS)) {
            block.setHandsStyle(appearance.getStyle(Appearance.HANDS));
        }

        if(appearance.isVisible(Appearance.LEGS)) {
            block.setLegsStyle(appearance.getStyle(Appearance.LEGS));
        }

        if(appearance.isVisible(Appearance.FEET)) {
            block.setFeetStyle(appearance.getStyle(Appearance.FEET));
        }

        return block;
    }

    /**
     *
     */
    private boolean male;

    /**
     *
     */
    private int headStyle = -1;
    private int beardStyle = -1;
    private int bodyStyle = -1;
    private int armsStyle = -1;
    private int handsStyle = -1;
    private int legsStyle = -1;
    private int feetStyle = -1;

    /**
     *
     */
    private int helmetId = -1;
    private int capeId = -1;
    private int neckId = -1;
    private int weaponId = -1;
    private int chestId = -1;
    private int shieldId = -1;
    private int pantsId = -1;
    private int glovesId = -1;
    private int shoesId = -1;

    /**
     * The combat level.
     */
    private int combatLevel;

    /**
     * The name.
     */
    private long name = -1L;

    /**
     * Constructs a new {@link AppearanceUpdateBlock};
     */
    public AppearanceUpdateBlock() {}

    /**
     *
     * @param male
     */
    public void setMale(boolean male) {
        this.male = male;
    }

    /**
     *
     * @return
     */
    public boolean isMale() {
        return male;
    }

    /**
     *
     * @param style
     */
    public void setHeadStyle(int style) {
        headStyle = style;
    }

    /**
     *
     * @return
     */
    public int getHeadStyle() {
        return headStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasHeadStyle() {
        return headStyle != -1;
    }

    /**
     *
     * @param style
     */
    public void setBeardStyle(int style) {
        beardStyle = style;
    }

    /**
     *
     * @return
     */
    public int getBeardStyle() {
        return beardStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasBeardStyle() {
        return beardStyle != -1;
    }

    /**
     *
     * @param style
     */
    public void setBodyStyle(int style) {
        bodyStyle = style;
    }

    /**
     *
     * @return
     */
    public int getBodyStyle() {
        return bodyStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasBodyStyle() {
        return bodyStyle != -1;
    }

    /**
     *
     * @param style
     */
    public void setArmsStyle(int style) {
        armsStyle = style;
    }

    /**
     *
     * @return
     */
    public int getArmsStyle() {
        return armsStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasArmsStyle() {
        return armsStyle != -1;
    }
    
    /**
     *
     * @param style
     */
    public void setHandsStyle(int style) {
        handsStyle = style;
    }

    /**
     *
     * @return
     */
    public int getHandsStyle() {
        return handsStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasHandsStyle() {
        return handsStyle != -1;
    }

    /**
     *
     * @param style
     */
    public void setLegsStyle(int style) {
        legsStyle = style;
    }

    /**
     *
     * @return
     */
    public int getLegsStyle() {
        return legsStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasLegsStyle() {
        return legsStyle != -1;
    }

    /**
     *
     * @param style
     */
    public void setFeetStyle(int style) {
        feetStyle = style;
    }

    /**
     *
     * @return
     */
    public int getFeetStyle() {
        return feetStyle;
    }

    /**
     *
     * @return
     */
    public boolean hasFeetStyle() {
        return feetStyle != -1;
    }

    /**
     *
     * @param helmet
     */
    public void setHelmetId(int helmet) {
        helmetId = helmet;
    }

    /**
     * 
     * @return
     */
    public int getHelmetId() {
        return helmetId;
    }

    /**
     *
     * @return
     */
    public boolean hasHelmet() {
        return helmetId != -1;
    }

    /**
     *
     * @param cape
     */
    public void setCapeId(int cape) {
        capeId = cape;
    }

    /**
     *
     * @return
     */
    public int getCapeId() {
        return capeId;
    }

    /**
     *
     * @return
     */
    public boolean hasCape() {
        return capeId != -1;
    }

    /**
     *
     * @param neck
     */
    public void setNecklaceId(int neck) {
        neckId = neck;
    }

    /**
     *
     * @return
     */
    public int getNecklaceId() {
        return neckId;
    }

    /**
     *
     * @return
     */
    public boolean hasNecklace() {
        return neckId != -1;
    }

    /**
     *
     * @param weapon
     */
    public void setWeaponId(int weapon) {
        weaponId = weapon;
    }

    /**
     *
     * @return
     */
    public int getWeaponId() {
        return weaponId;
    }

    /**
     *
     * @return
     */
    public boolean hasWeapon() {
        return weaponId != -1;
    }

    /**
     *
     * @param chest
     */
    public void setChestId(int chest) {
        chestId = chest;
    }

    /**
     *
     * @return
     */
    public int getChestId() {
        return chestId;
    }

    /**
     *
     * @return
     */
    public boolean hasChest() {
        return chestId != -1;
    }

    /**
     *
     * @param pants
     */
    public void setPantsId(int pants) {
        pantsId = pants;
    }

    /**
     *
     * @return
     */
    public int getPantsId() {
        return pantsId;
    }

    /**
     *
     * @return
     */
    public boolean hasPants() {
        return pantsId != -1;
    }

    /**
     *
     * @param gloves
     */
    public void setGlovesId(int gloves) {
        glovesId = gloves;
    }

    /**
     *
     * @return
     */
    public int getGlovesId() {
        return glovesId;
    }

    /**
     *
     * @return
     */
    public boolean hasGloves() {
        return glovesId != -1;
    }

    /**
     *
     * @param shoes
     */
    public void setShoesId(int shoes) {
        shoesId = shoes;
    }

    /**
     *
     * @return
     */
    public int getShoesId() {
        return shoesId;
    }

    /**
     *
     * @return
     */
    public boolean hasShoes() {
        return shoesId != -1;
    }

    /**
     *
     * @param shield
     */
    public void setShieldId(int shield) {
        shieldId = shield;
    }

    /**
     *
     * @return
     */
    public int getShieldId() {
        return shieldId;
    }

    /**
     *
     * @return
     */
    public boolean hasShield() {
        return shieldId != -1;
    }

    /**
     *
     * @param combatLevel
     */
    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    /**
     * Gets the combat level.
     *
     * @return the combat level.
     */
    public int getCombatLevel() {
        return combatLevel;
    }

    /**
     *
     * @param name
     */
    public void setName(long name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public long getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public int getStance() {
        return 1426;
    }
}
